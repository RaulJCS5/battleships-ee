package isel.pdm.ee.battleship.game.domain

data class Game(
    val localPlayerMarker: PlayerMarker = PlayerMarker.firstToMove,
    val quitGameBy: PlayerMarker? = null,
    val board: Board = Board()
)

fun Game.makeMove(at: Coordinate): Game {
    check(localPlayerMarker == board.turn)
    return copy(board = board.makeMove(at))
}

fun Game.getResult() =
    if (quitGameBy != null) HasWinner(quitGameBy.other)
    else board.getResult()