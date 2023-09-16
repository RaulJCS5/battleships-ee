package isel.pdm.ee.battleship.game.domain

const val BOARD_SIDE = 10
/**
 * Represents coordinates in the board.
 * @property row The row of the coordinate.
 * @property column The column of the coordinate.
 */
data class Coordinate(val row: Int, val column: Int) {
    init {
        require(isValidRow(row) && isValidColumn(column))
    }

    override fun toString(): String {
        return "($row,$column)"
    }
}

/**
 * Checks whether [value] is a valid row index
 */
fun isValidRow(value: Int) = value in 0 until BOARD_SIDE

/**
 * Checks whether [value] is a valid column index
 */
fun isValidColumn(value: Int) = value in 0 until BOARD_SIDE

