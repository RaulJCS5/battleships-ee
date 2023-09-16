package isel.pdm.ee.battleship.fleet.domain

import isel.pdm.ee.battleship.game.domain.Ship
import isel.pdm.ee.battleship.game.domain.ShipType

enum class Layout{
    L,R,U,D,N
}

class AllShips() {
    val shipList = Result.success(mutableListOf<Ship>())
    init {
        shipList.getOrNull()?.add(Ship(ShipType.CARRIER, null, "N"))
        shipList.getOrNull()?.add(Ship(ShipType.BATTLESHIP, null, "N"))
        shipList.getOrNull()?.add(Ship(ShipType.CRUISER, null, "N"))
        shipList.getOrNull()?.add(Ship(ShipType.SUBMARINE, null, "N"))
        shipList.getOrNull()?.add(Ship(ShipType.DESTROYER, null, "N"))
    }
}