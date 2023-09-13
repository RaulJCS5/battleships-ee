package isel.pdm.ee.battleship.game.domain


data class Board(
    val turn: PlayerMarker = PlayerMarker.firstToMove,
    val tiles: MutableList<MutableList<PositionStateBoard>> =
        MutableList(
            size = BOARD_SIDE,
            init = { row->
                MutableList(size = BOARD_SIDE, init = { col ->
                    PositionStateBoard(
                        boardPosition = Coordinate(row, col),
                        wasShoot = false, wasShip = false, shipType = null, shipLayout = null
                    )
                }) }
        )
) {

    operator fun get(at: Coordinate): PositionStateBoard = getMove(at)

    fun getMove(at: Coordinate): PositionStateBoard = tiles[at.row][at.column]

    fun makeMove(at: Coordinate): Board {
        return Board(
            turn = turn.other,
            tiles = tiles.mapIndexed { row, elem ->
                if (row == at.row)
                    MutableList(tiles[row].size) { col ->
                        if (col == at.column) PositionStateBoard(
                            boardPosition = Coordinate(row, col),
                            wasShoot = true,
                            wasShip = false,
                            shipType = null,
                            shipLayout = null
                        )
                        else tiles[row][col]
                    }
                else
                    elem
            }.toMutableList()
        )
    }
    fun toMovesList(): MutableList<PositionStateBoard?> = tiles.flatten().toMutableList()
}

fun isTied(): Boolean = false

fun hasWon(playerMarker: PlayerMarker): Boolean = false


open class BoardResult
class HasWinner(val winner: PlayerMarker) : BoardResult()
class Tied : BoardResult()
class OnGoing : BoardResult()

fun Board.getResult(): BoardResult =
    when {
        hasWon(PlayerMarker.PLAYER1) -> HasWinner(PlayerMarker.PLAYER1)
        hasWon(PlayerMarker.PLAYER2) -> HasWinner(PlayerMarker.PLAYER2)
        toMovesList().all { it != null } -> Tied()
        else -> OnGoing()
    }