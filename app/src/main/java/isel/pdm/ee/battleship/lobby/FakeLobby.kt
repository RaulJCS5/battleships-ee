package isel.pdm.ee.battleship.lobby

import isel.pdm.ee.battleship.lobby.domain.*
import isel.pdm.ee.battleship.preferences.domain.UserInfo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class FakeLobby() : Lobby {
    private var state: LobbyState = Idle
    private val list = mutableListOf<PlayerInfo>()

    init {
        repeat(10) {
            list.add(PlayerInfo(UserInfo("Nick$it", "Moto$it")))
        }
    }
    override suspend fun getPlayers(): List<PlayerInfo> {
        return list
    }
    /**
     * The coroutine that calls this method will be suspended until the lobby is left or a player added to the lobby.
     * */
    override suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo> {
        check(state == Idle)
        try {
            list.add(localPlayer)
            state = InUse(localPlayer)
            return getPlayers()
        } catch (e: Throwable) {
            throw Exception()
        }
    }
    /**
     *  Enter the lobby and observe the changes in the lobby.
     *  callbackFlow is a builder function that creates a Flow whose lifecycle is controlled by the given ProducerScope.
     *  It is a primitive that serves to adapt the reality of asynchronous APIs based on callbacks to the reality of asynchronous APIs based on suspending coroutines.
     *  As? Giving programmatic access to continuation
     *  When the callbackFlow is terminated, the collect is returned.
     *  Consequently the writes in the collect flow are stopped.
     * */
    override fun enterAndObserve(localPlayer: PlayerInfo): Flow<LobbyEvent> {
        check(state == Idle)
        return callbackFlow {
            state = InUseWithFlow(localPlayer, scope = this)
            try {
                list.add(localPlayer)
                delay(5000)
                trySend(PlayersInLobbyUpdate(getPlayers()))
            } catch (e: Exception) {
                close(e)
            }
            awaitClose {
                list.remove(localPlayer)
            }
        }
    }

    /**
     * If not inside the lobby, throws an exception.
     * If InUse without flow, removes the local player from the lobby.
     * If InUse with flow, closes the flow and removes the local player from the lobby.
     * */
    override suspend fun leave() {
        when (val currentState = state) {
            is InUseWithFlow -> {
                currentState.scope.close()
            }

            is InUse -> {
                list.remove(currentState.localPlayer)
            }

            is Idle -> throw IllegalStateException()
        }
        state = Idle
    }
}