package isel.pdm.ee.battleship.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.game.Coordinate
import isel.pdm.ee.battleship.game.PositionStateBoard
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme


internal const val TileViewTag = "TileView"

@Composable
fun TileView(
    move: PositionStateBoard?,
    enabled: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (move != null) {
        val marker = getTile(move)
        Box(modifier = modifier
            .background(marker)
            .fillMaxSize(1.0f)
            .padding(4.dp)
            .clickable(enabled = !move.wasShoot) { onSelected() }
        ) {
        }
    }
}

fun getTile(move: PositionStateBoard): Color {
    return if (move.wasShoot){
        if (move.wasShip==true){
            Color.Red
        } else{
            Color.White
        }
    }else{
        if (move.wasShip==true){
            Color.Gray
        } else{
            Color.Blue
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TileViewEmptyPreview() {
    BattleshipTheme {
        TileView(
            move = PositionStateBoard(
                boardPosition = Coordinate(0,0),
                wasShip = false,
                wasShoot = false,
                shipType = null,
                shipLayout = null
            ),
            enabled = true,
            onSelected = { }
        )
    }
}