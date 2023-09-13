package isel.pdm.ee.battleship.game.domain

import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import kotlinx.coroutines.flow.Flow


sealed class GameEvent(val game: Game)
class GameStarted(game: Game) : GameEvent(game)
class MoveMade(game: Game) : GameEvent(game)
class GameEnded(game: Game, val winner: PlayerMarker? = null) : GameEvent(game)

interface Match {

    fun start(localPlayer: PlayerInfo, matching: Matching): Flow<GameEvent>
    suspend fun forfeit()
    suspend fun end()
    suspend fun makeMove(at: Coordinate)
}

