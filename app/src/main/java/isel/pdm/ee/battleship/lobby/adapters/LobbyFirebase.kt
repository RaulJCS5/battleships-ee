package isel.pdm.ee.battleship.lobby.adapters

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import isel.pdm.ee.battleship.lobby.domain.Idle
import isel.pdm.ee.battleship.lobby.domain.InUse
import isel.pdm.ee.battleship.lobby.domain.InUseWithFlow
import isel.pdm.ee.battleship.lobby.domain.Lobby
import isel.pdm.ee.battleship.lobby.domain.LobbyEvent
import isel.pdm.ee.battleship.lobby.domain.LobbyState
import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.MatchingReceived
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import isel.pdm.ee.battleship.lobby.domain.PlayersInLobbyUpdate
import isel.pdm.ee.battleship.preferences.domain.UserInfo
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID


const val LOBBY = "lobby"
const val MATCHING = "matching"
const val NICK_FIELD = "nick"
const val MOTO_FIELD = "moto"
const val MATCHING_ID = "id"

class LobbyFirebase(private val db: FirebaseFirestore) : Lobby {
    private var state: LobbyState = Idle
    override suspend fun getPlayers(): List<PlayerInfo> {
        try {
            val result = db.collection(LOBBY).get().await()
            return result.map { it.toPlayerInfo() }
        } catch (e: Throwable) {
            throw Exception()
        }
    }

    override suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo> {
        check(state == Idle)
        try {
            state = InUse(localPlayer, addLocalPlayer(localPlayer))
            return getPlayers()
        } catch (e: Throwable) {
            throw Exception()
        }
    }

    override suspend fun leave() {
        when (val currentState = state) {
            is InUseWithFlow -> {
                currentState.scope.close()
            }

            is InUse -> {
                currentState.localPlayerDocRef?.delete()?.await()
            }

            is Idle -> throw IllegalStateException()
        }
        state = Idle
    }

    /**
     * Query observable
     * I enter the lobby and now I will be interest to receive information of alteration to the list of players in the lobby
     * each time theres a change I promise that I will be notified
     *
     * */
    override fun enterAndObserve(localPlayer: PlayerInfo): Flow<LobbyEvent> {
        check(state == Idle)
        return callbackFlow {
            state = InUseWithFlow(localPlayer, scope = this)
            var myDocRef: DocumentReference? = null
            var playersObserve: ListenerRegistration? = null
            var matchingObserve: ListenerRegistration? = null
            try {
                myDocRef = addLocalPlayer(localPlayer) // adds the local player to the lobby
                playersObserve = playersObserveUpdated(flow = this)
                matchingObserve = matchingObserveUpdated(myDocRef, flow = this)
            } catch (e: Exception) {
                close(e)
            }
            awaitClose { // guarantees that the local player is removed from the lobby
                playersObserve?.remove()
                matchingObserve?.remove()
                myDocRef?.delete()
            }
        }
    }

    private fun matchingObserveUpdated(
        myDocRef: DocumentReference,
        flow: ProducerScope<LobbyEvent>
    ): ListenerRegistration {
        return myDocRef.addSnapshotListener(){
            snapshot, error ->
            when {
                error != null -> flow.close(error)
                snapshot != null -> {
                    val matching: Matching? = snapshot.toMatchingOrNull()
                    if (matching != null) {
                        flow.trySend(MatchingReceived(matching))
                    }
                }
            }
        }
    }

    override suspend fun createMatching(to: PlayerInfo): Matching {
        val localPlayer = when (val currentState = state) {
            is Idle -> throw java.lang.IllegalStateException()
            is InUse -> currentState.localPlayer
            is InUseWithFlow -> currentState.localPlayer
        }
        db.collection(LOBBY)
            .document(to.id.toString())
            .update(MATCHING, localPlayer.toDocumentContent())
            .await()
        return Matching(player1 = localPlayer, player2 = to)
    }

    private suspend fun addLocalPlayer(localPlayer: PlayerInfo): DocumentReference {
        val docRef = db.collection(LOBBY).document(localPlayer.id.toString())
        docRef
            .set(localPlayer.info.toDocumentContent())
            .await()
        return docRef
    }

    private fun playersObserveUpdated(flow: ProducerScope<LobbyEvent>) =
        db.collection(LOBBY).addSnapshotListener { snapshot, error ->
            when {
                error != null -> flow.close(error)
                snapshot != null -> flow.trySend(PlayersInLobbyUpdate(snapshot.toPlayerList()))
            }
        }
}

private fun DocumentSnapshot.toMatchingOrNull(): Matching? {
    val docData = data
    if (docData != null) {
        val matching = docData[MATCHING] as Map<*, *>?
        if (matching != null) {
            return Matching(
                player1 = PlayerInfo(
                    info = UserInfo(
                        nick = matching[NICK_FIELD] as String,
                        moto = matching[MOTO_FIELD] as String?
                    ),
                    id = UUID.fromString(
                        matching[MATCHING_ID] as String
                    )
                ),
                player2 = PlayerInfo(
                    info = UserInfo(
                        nick = this[NICK_FIELD] as String,
                        moto = this[MOTO_FIELD] as String?
                    ),
                    id = UUID.fromString(this.id)
                )
            )
        }
    }
    return null
}

/**
 * [PlayerInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun PlayerInfo.toDocumentContent() = mapOf(
    NICK_FIELD to info.nick,
    MOTO_FIELD to info.moto,
    MATCHING_ID to id.toString()
)

fun QueryDocumentSnapshot.toPlayerInfo() =
    PlayerInfo(
        info = UserInfo(
            nick = data[NICK_FIELD] as String,
            moto = data[MOTO_FIELD] as String?
        ),
        id = UUID.fromString(id),
    )

fun QuerySnapshot.toPlayerList() = map { it.toPlayerInfo() }

/**
 * [UserInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun UserInfo.toDocumentContent() = mapOf(
    NICK_FIELD to nick,
    MOTO_FIELD to moto
)
