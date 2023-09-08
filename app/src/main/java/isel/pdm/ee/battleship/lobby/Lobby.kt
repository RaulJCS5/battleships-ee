package isel.pdm.ee.battleship.lobby

import isel.pdm.ee.battleship.preferences.domain.UserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

/**
 * Abstraction that characterizes the game's lobby.
 */
interface Lobby {
    /**
     * Gets the list of players currently in the lobby
     * @return the list of players currently in the lobby
     */
    suspend fun getPlayers(): List<PlayerInfo>

    val players: Flow<List<PlayerInfo>>

    /**
     * Enters the lobby. It cannot be entered again until left.
     * @return the list of players currently in the lobby
     * @throws IllegalStateException    if the lobby was already entered
     */
    suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo>

    /**
     * Leaves the lobby. If the lobby was entered using [enterAndObserve],
     * the returned flow is closed, thereby ending the flow of lobby events.
     * @throws IllegalStateException    if the lobby was not yet entered
     */
    suspend fun leave()
}

class FakeLobby : Lobby {
    private var count = 0

    override suspend fun getPlayers(): List<PlayerInfo> {
        val list = buildList {
            repeat(5) {
                add(PlayerInfo(UserInfo("Nick$it", "$count This is my $it moto"), id = UUID.randomUUID()))
            }
        }
        count++
        return list
    }

    override suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo> {
        TODO()
    }

    override suspend fun leave() {
        TODO()
    }

    override val players: Flow<List<PlayerInfo>>
        get() = flow {
            while(true) {
                delay(5000)
                emit(getPlayers())
            }
        }
}