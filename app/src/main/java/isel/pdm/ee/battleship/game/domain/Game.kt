package isel.pdm.ee.battleship.game.domain

data class Game(
    val localPlayerMarker: PlayerMarker = PlayerMarker.firstToMove,
    val forfeitedBy: PlayerMarker? = null,
    val board: Board = Board()
)

fun Game.makeMove(at: Coordinate): Game {
    check(localPlayerMarker == board.turn)
    return copy(board = board.makeMove(at))
}

fun Game.getResult() =
    if (forfeitedBy != null) HasWinner(forfeitedBy.other)
    else board.getResult()