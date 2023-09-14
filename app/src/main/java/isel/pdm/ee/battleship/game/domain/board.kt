package isel.pdm.ee.battleship.game.domain


const val BOARD_FIELD= "board"
const val TURN_FIELD = "turn"
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
    fun toDocumentContent() = mapOf(
        TURN_FIELD to turn.name,
        BOARD_FIELD to toMovesList().joinToString(separator = "") {
            when {
                it?.wasShoot == true && it.wasShip == true -> "X"
                it?.wasShoot == true && it.wasShip == false -> "O"
                else -> "-"
            }
        }
    )

    companion object {
        fun fromMovesList(valueOf: PlayerMarker, moves: String): Board {
            return Board(
                turn = valueOf,
                tiles = MutableList(
                    size = BOARD_SIDE,
                    init = { row ->
                        MutableList(size = BOARD_SIDE, init = { col ->
                            when (moves[row * BOARD_SIDE + col]) {
                                'X' -> PositionStateBoard(
                                    boardPosition = Coordinate(row, col),
                                    wasShoot = true,
                                    wasShip = true,
                                    shipType = null,
                                    shipLayout = null
                                )

                                'O' -> PositionStateBoard(
                                    boardPosition = Coordinate(row, col),
                                    wasShoot = true,
                                    wasShip = false,
                                    shipType = null,
                                    shipLayout = null
                                )

                                else -> PositionStateBoard(
                                    boardPosition = Coordinate(row, col),
                                    wasShoot = false,
                                    wasShip = false,
                                    shipType = null,
                                    shipLayout = null
                                )
                            }
                        })
                    })
            )
        }
    }
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