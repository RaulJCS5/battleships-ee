package isel.pdm.ee.battleship.game.domain

/**
 * Represents the tiles of the board.
 * @property boardPosition The position of the tile in the board.
 * @property wasShoot Whether the tile was shot or not.
 * @property wasShip Whether the tile was a ship or not.
 * @property shipType The type of the ship.
 * @property shipLayout The layout of the ship.
 */
data class PositionStateBoard(
    val boardPosition: Coordinate,
    var wasShoot: Boolean,
    var wasShip: Boolean?,
    var shipType: ShipType?,
    var shipLayout: String?
)