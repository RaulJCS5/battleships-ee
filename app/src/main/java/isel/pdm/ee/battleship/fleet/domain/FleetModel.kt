package isel.pdm.ee.battleship.fleet.domain

import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.Ship
import isel.pdm.ee.battleship.game.domain.ShipType

enum class Layout{
    L,R,U,D,N
}

class AllShips() {
    val shipList = Result.success(mutableListOf<Ship>())
    fun fillEmptyShipList() {
        shipList.getOrNull()?.add(Ship(ShipType.CARRIER, null, "N"))
        shipList.getOrNull()?.add(Ship(ShipType.BATTLESHIP, null, "N"))
        shipList.getOrNull()?.add(Ship(ShipType.CRUISER, null, "N"))
        shipList.getOrNull()?.add(Ship(ShipType.SUBMARINE, null, "N"))
        shipList.getOrNull()?.add(Ship(ShipType.DESTROYER, null, "N"))
    }
    init {
        fillEmptyShipList()
    }
    fun fillFakeShipList(){
        shipList.getOrNull()?.add(Ship(ShipType.CARRIER, Coordinate(0,0), "D"))
        shipList.getOrNull()?.add(Ship(ShipType.BATTLESHIP, Coordinate(0,1), "D"))
        shipList.getOrNull()?.add(Ship(ShipType.CRUISER, Coordinate(0,2), "D"))
        shipList.getOrNull()?.add(Ship(ShipType.SUBMARINE, Coordinate(0,3), "D"))
        shipList.getOrNull()?.add(Ship(ShipType.DESTROYER, Coordinate(0,4), "D"))
    }
}