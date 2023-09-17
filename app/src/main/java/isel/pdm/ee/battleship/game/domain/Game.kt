package isel.pdm.ee.battleship.game.domain

/**
 * Represents a game. Instances are immutable.
 * @property localPlayerMarker The local player marker
 * @property quitGameBy The marker of the player who quit the game, if that was the case
 * @property board The board that contains the shots at the opponent board.
 */
data class Game(
    val localPlayerMarker: PlayerMarker = PlayerMarker.firstToMove,
    val quitGameBy: PlayerMarker? = null,
    val board: Board = Board()
)

/**
 * Makes a move on this [Game], returning a new instance.
 * @param at the coordinates where the move is to be made
 * @return the new [Game] instance
 * TODO: @throws IllegalStateException if its an invalid move, either because its not the local player's turn or the move cannot be made on that location
 */
fun Game.makeMove(at: Coordinate, mutableFleetList: MutableList<Ship>?): Game {
    check(localPlayerMarker == board.turn && mutableFleetList != null)
    return copy(board = board.makeMove(at, mutableFleetList))
}

/**
 * Gets the game current result
 * @return The result of the game.
 */
fun Game.getResult() =
    if (quitGameBy != null) HasWinner(quitGameBy.other)
    else board.getResult()