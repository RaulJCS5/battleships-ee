package isel.pdm.ee.battleship.game.domain

import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import kotlinx.coroutines.flow.Flow

/**
 * Sum type used to describe events occurring while the match is ongoing.
 *
 * [GameStarted] to signal that the game has started.
 * [MoveMade] to signal that the a move was made.
 * [GameEnded] to signal the game termination.
 */
sealed class GameEvent(val game: Game)
class GameStarted(game: Game) : GameEvent(game)
class MoveMade(game: Game) : GameEvent(game)
class GameEnded(game: Game, val winner: PlayerMarker? = null) : GameEvent(game)

/**
 * Sum type used to describe events occurring while the timer is ongoing.
 *
 * [TimeUpdated] to signal that the time has been updated.
 * [TimeEnded] to signal that the time has ended.
 */
sealed class TimeEvent(val time: Int)
class TimeUpdated(time: Int) : TimeEvent(time)
class TimeEnded(time: Int) : TimeEvent(time)

/**
 * Abstraction that characterizes a match between two players, that is, the
 * required interactions.
 */
interface Match {

    /**
     * Starts the match. The first to make a move is the PLAYER1. The game
     * is only actually in progress after its initial state is published on the flow.
     * @param [localPlayer] the local player information
     * @param [matching] the matching bearing the players' information
     * @return the flow of game state change events, expressed as [GameEvent] instances
     * TODO: @throws IllegalStateException if a game is in progress
     */
    fun startAndObserveGameEvents(localPlayer: PlayerInfo, matching: Matching): Flow<GameEvent>
    /**
     * Quits the current game.
     * TODO: @throws IllegalStateException if a game is not in progress
     */
    suspend fun quitGame()
    /**
     * Ends the match, cleaning up if necessary.
     */
    suspend fun end()
    /**
     * Makes a move at the given coordinates.
     * TODO: @throws IllegalStateException if a game is not in progress or the move is illegal, either because it's not the local player turn or the position is not free.
     */
    suspend fun makeMove(at: Coordinate)
    /**
     * Starts the timer.
     * @param [time] the time to start the timer
     * @param [timeLimit] the time limit of the timer
     * @return the flow of time state change events, expressed as [TimeEvent] instances
     * TODO: @throws IllegalStateException if a game is in progress
     */
    fun startTimer(time: Int, timeLimit: Int): Flow<TimeEvent>

    /**
     * Saves the game.
     * @param [localPlayer] the local player information
     */
    suspend fun saveGameAndUpdate(localPlayer: PlayerInfo, resultWinner: String)
}

