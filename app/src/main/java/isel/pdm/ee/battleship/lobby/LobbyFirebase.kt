package isel.pdm.ee.battleship.lobby

import com.google.firebase.firestore.DocumentReference
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
const val NICK_FIELD = "nick"
const val MOTO_FIELD = "moto"

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
            try {
                myDocRef = addLocalPlayer(localPlayer) // adds the local player to the lobby
                playersObserve = playersObserveUpdated(flow = this)
            } catch (e: Exception) {
                close(e)
            }
            awaitClose { // guarantees that the local player is removed from the lobby
                playersObserve?.remove()
                myDocRef?.delete()
            }
        }
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
