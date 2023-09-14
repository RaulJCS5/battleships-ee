package isel.pdm.ee.battleship.game.domain

import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import kotlinx.coroutines.flow.Flow


sealed class GameEvent(val game: Game)
class GameStarted(game: Game) : GameEvent(game)
class MoveMade(game: Game) : GameEvent(game)
class GameEnded(game: Game, val winner: PlayerMarker? = null) : GameEvent(game)

sealed class TimeEvent(val time: Int)
class TimeUpdated(time: Int) : TimeEvent(time)
class TimeEnded(time: Int) : TimeEvent(time)

interface Match {

    fun startAndObserveGameEvents(localPlayer: PlayerInfo, matching: Matching): Flow<GameEvent>
    suspend fun quitGame()
    suspend fun end()
    suspend fun makeMove(at: Coordinate)
    fun startTimer(time: Int): Flow<TimeEvent>
}

