package isel.pdm.ee.battleship.fleet.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import isel.pdm.ee.battleship.fleet.domain.AllShips
import isel.pdm.ee.battleship.game.domain.Board
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.PositionStateBoard
import isel.pdm.ee.battleship.game.domain.Ship
import isel.pdm.ee.battleship.game.domain.getSizeByName
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import isel.pdm.ee.battleship.preferences.domain.UserInfoRepository


class FleetViewModel(
    val userInfoRepo: UserInfoRepository
) : ViewModel(){
    fun deleteAll() {
        try {
            _allShipsAndLayouts = AllShips().shipList
            _fleetBoard = Result.success(Board())
        }catch (e:Exception){
            _allShipsAndLayouts = Result.failure(e)
            _fleetBoard = Result.failure(e)
        }

    }

    fun setShipLayout(ship: String, layout: String) {
        try {
            val updated = _allShipsAndLayouts?.getOrNull()?.map {
                if (it.shipType.name == ship&&it.coordinate==null) {
                    it.copy(orientation = layout)
                } else {
                    it
                }
            }
            if (updated!=null)
                _allShipsAndLayouts = Result.success(updated.toMutableList())
        }catch (e:Exception){
            _allShipsAndLayouts = Result.failure(e)
        }
    }

    fun updateFleetBoard(at: Coordinate) {
        try {
            val localPlayerUpdate = PlayerInfo(checkNotNull(userInfoRepo.userInfo))
            val listListMarker = fleetBoard?.getOrNull()?.tiles
            allShipsAndLayouts?.getOrNull()?.map {
                if (it.coordinate == null) {
                    val shipSize = getSizeByName(it.shipType.name)-1
                    listListMarker?.mapIndexed { row, listMarker ->
                        if (it.orientation.equals("U")) {
                            for (i in 0..shipSize) {
                                if (row == at.row - i) {
                                    listMarker[at.column] = PositionStateBoard(at,
                                        wasShoot = false,
                                        wasShip = true,
                                        null,
                                        null
                                    )
                                }
                            }
                        } else if (it.orientation.equals("D")) {
                            for (i in 0..shipSize) {
                                if (row == at.row + i) {
                                    listMarker[at.column] = PositionStateBoard(at,
                                        wasShoot = false,
                                        wasShip = true,
                                        null,
                                        null
                                    )
                                }
                            }
                        } else if (it.orientation.equals("L")) {
                            for (i in 0..shipSize) {
                                if (row == at.row) {
                                    listMarker[at.column - i] = PositionStateBoard(at,
                                        wasShoot = false,
                                        wasShip = true,
                                        null,
                                        null
                                    )
                                }
                            }
                        } else if (it.orientation.equals("R")) {
                            for (i in 0..shipSize) {
                                if (row == at.row) {
                                    listMarker[at.column + i] = PositionStateBoard(at,
                                        wasShoot = false,
                                        wasShip = true,
                                        null,
                                        null
                                    )
                                }
                            }
                        }
                    }
                }
            }
            setShipLayoutReferenceLayout(at)
            if (listListMarker != null) {
                _fleetBoard = Result.success(
                    Board(
                        tiles = listListMarker,
                        //ships = allShipsAndLayouts?.getOrNull()?.filter { it.referencePoint != null }
                    )
                )
            }
        }catch (e:Exception){
            _fleetBoard = Result.failure(e)
        }
    }
    private fun setShipLayoutReferenceLayout(at:Coordinate) {
        val updated = _allShipsAndLayouts?.getOrNull()?.map {
            if (!it.orientation.equals("N")&&it.coordinate==null) {
                it.copy(coordinate = at)
            } else {
                it
            }
        }
        if (updated!=null)
            _allShipsAndLayouts = Result.success(updated.toMutableList())
    }

    private var _allShipsAndLayouts by mutableStateOf<Result<MutableList<Ship>>?>(
        AllShips().shipList)
    val allShipsAndLayouts: Result<MutableList<Ship>>?
        get() = _allShipsAndLayouts

    private var _fleetBoard by mutableStateOf<Result<Board>?>(Result.success(Board()))
    val fleetBoard: Result<Board>?
        get() = _fleetBoard

}