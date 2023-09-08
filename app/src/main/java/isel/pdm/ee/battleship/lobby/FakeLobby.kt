package isel.pdm.ee.battleship.lobby

import isel.pdm.ee.battleship.lobby.domain.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class FakeLobby : Lobby {
    private var state: LobbyState = Idle
    private val list = mutableListOf<PlayerInfo>()
    override suspend fun getPlayers(): List<PlayerInfo> {
        return list
    }

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