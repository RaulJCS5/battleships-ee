package isel.pdm.ee.battleship.game.domain

const val BOARD_SIDE = 10

data class Coordinate(val row: Int, val column: Int) {
    init {
        require(isValidRow(row) && isValidColumn(column))
    }

    override fun toString(): String {
        return "($row,$column)"
    }
}

fun isValidRow(value: Int) = value in 0 until BOARD_SIDE

fun isValidColumn(value: Int) = value in 0 until BOARD_SIDE

