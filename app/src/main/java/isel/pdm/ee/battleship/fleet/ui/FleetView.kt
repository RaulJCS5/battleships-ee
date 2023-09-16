package isel.pdm.ee.battleship.fleet.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.fleet.domain.AllShips
import isel.pdm.ee.battleship.fleet.domain.Layout
import isel.pdm.ee.battleship.game.domain.Ship

@Composable
fun FleetView(
    fleet: MutableList<Ship>,
    onClickFleetLayout: (String,String) -> Unit = { _: String, _: String -> }
) {
    fleet.map {
        val shipAndLayout = it
        val ship = shipAndLayout.shipType
        Text(text = ship.name, style = MaterialTheme.typography.titleSmall)
        Row {
            Layout.values().map {
                val layout = it.name
                Button(
                    onClick = { onClickFleetLayout(ship.name,layout) } ,
                    enabled = layout!=shipAndLayout.orientation,
                    modifier = Modifier.size(60.dp, 40.dp) // Adjust the size as needed
                ) {
                    Text(text = layout)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonsFleetView() {
    FleetView(fleet = AllShips().shipList.getOrNull()!!)
}
