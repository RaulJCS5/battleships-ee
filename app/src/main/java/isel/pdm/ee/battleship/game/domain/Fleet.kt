package isel.pdm.ee.battleship.game.domain

// "(CRUISER,(4,4),L))"
// orientation -> L, R, U, D

/**
 * Represents a ship in the board.
 * @property shipType The type of the ship.
 * @property coordinate The coordinate of the ship.
 * @property orientation The orientation of the ship.
 * TODO: @property isSunken not being used
 */
data class Ship(
    val shipType: ShipType,
    val coordinate: Coordinate,
    val orientation: String,
    val isSunken: Boolean = false
)
