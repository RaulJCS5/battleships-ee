package isel.pdm.ee.battleship.game.domain
/**
 * Enumeration type used to represent the game's ships.
 * @property size The size of the ship.
 * @property name The name of the ship.
 */
enum class ShipType(val size: Int, name: String) {
    CARRIER(5, "CARRIER"),
    BATTLESHIP(4, "BATTLESHIP"),
    SUBMARINE(3, "SUBMARINE"),
    CRUISER(3, "CRUISER"),
    DESTROYER(2, "DESTROYER");

    companion object {

        /**
         * Gets the sum of all ships' sizes.
         * @return The sum of all ships' sizes.
         * @see ShipType.size
         */
        fun getShipsAllTiles(): Int {
            return ShipType.values().sumOf { it.size }
        }
    }
}

/**
 * Gets the size of the ship by its name.
 * @param name The name of the ship.
 * @return The size of the ship.
 */
fun getSizeByName(name: String): Int {
    return ShipType.values().find { it.name == name }!!.size
}