package isel.pdm.ee.battleship.fleet.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.lobby.ui.LobbyActivity
import isel.pdm.ee.battleship.utils.viewModelInit

class FleetActivity: ComponentActivity() {
    private val viewModel by viewModels<FleetViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            FleetViewModel(app.userInfoRepo)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, FleetActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val setFleetBoard = viewModel.fleetBoard?.getOrNull()
            val allShips = viewModel.allShipsAndLayouts?.getOrNull()
            FleetScreen(
                state = GameScreenState(
                    setFleetBoard = setFleetBoard,
                    allShips = allShips
                ),
                deleteAll = {
                    viewModel.deleteAll()
                },
                onClickFleetLayout = { ship: String, layout: String ->
                    viewModel.setShipLayout(ship, layout)
                },
                onPositionDefineFleet = { at: Coordinate ->
                    viewModel.updateFleetBoard(at)
                },
                fetchSetFleet = {
                    // TODO: Send fleet to the lobby
                    LobbyActivity.navigate(this)
                }
            )
        }
    }
}