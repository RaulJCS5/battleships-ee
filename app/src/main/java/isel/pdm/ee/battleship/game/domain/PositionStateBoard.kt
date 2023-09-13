package isel.pdm.ee.battleship.game.domain

data class PositionStateBoard(
    val boardPosition: Coordinate,
    var wasShoot: Boolean,
    var wasShip: Boolean?,
    var shipType: ShipType?,
    var shipLayout: String?
)