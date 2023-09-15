package isel.pdm.ee.battleship.game.domain

// "(CRUISER,(4,4),L))"
// orientation -> L, R, U, D

const val FLEET_FIELD= "fleet"

data class Ship(
    val shipType: ShipType,
    val coordinate: Coordinate,
    val orientation: String,
    val isSunken: Boolean = false
)
