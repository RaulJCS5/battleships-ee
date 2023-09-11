package isel.pdm.ee.battleship.game

const val BOARD_SIDE = 10

data class Coordinate(val row: Int, val column: Int) {
    init {
        require(isValidRow(row) && isValidColumn(column))
    }
}

fun isValidRow(value: Int) = value in 0 until BOARD_SIDE

fun isValidColumn(value: Int) = value in 0 until BOARD_SIDE

