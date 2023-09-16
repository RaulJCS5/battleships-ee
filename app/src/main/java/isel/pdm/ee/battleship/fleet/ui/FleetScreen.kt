package isel.pdm.ee.battleship.fleet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.fleet.domain.AllShips
import isel.pdm.ee.battleship.game.domain.Board
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.Ship
import isel.pdm.ee.battleship.game.ui.GameScreenTag
import isel.pdm.ee.battleship.ui.TopBar
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme

data class GameScreenState(
    val setFleetBoard: Board? = null,
    val allShips: MutableList<Ship>? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetScreen(
    state: GameScreenState,
    deleteAll: () -> Unit = { },
    onClickFleetLayout: (String,String) -> Unit = { _: String, _: String -> },
    onPositionDefineFleet: ( Coordinate) -> Unit = { _:Coordinate -> },
    fetchSetFleet: () -> Unit = { },
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(GameScreenTag),
            topBar = {
                TopBar(
                    navigationId = R.string.app_name
                )
            },
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                if (state.setFleetBoard != null && state.allShips != null) {
                    BoardFleetView(
                        board = state.setFleetBoard,
                        onPositionDefineFleet = onPositionDefineFleet,
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1.0f, true)
                            .fillMaxSize(),
                    )
                    FleetView(
                        fleet = state.allShips,
                        onClickFleetLayout = onClickFleetLayout
                    )
                    Row {
                        Button(onClick = deleteAll) {
                            Text(text = "Delete all")
                        }
                        if (state.allShips.find { it.coordinate == null } == null) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(onClick = fetchSetFleet) {
                                Text(text = "Set fleet")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefineFleetPreview() {
    FleetScreen(
        state = GameScreenState(
            setFleetBoard =  aBoard,
            allShips = AllShips().shipList.getOrNull(),
        )
    )
}

private val aBoard = Board()
