package isel.pdm.ee.battleship.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.game.PositionStateBoard


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
    if (move.wasShoot){
        if (move.wasShip==true){
            return Color.Red
        }
        else{
            return Color.White
        }
    }else{
        if (move.wasShip==true){
            return Color.Gray
        }
        else{
            return Color.Blue
        }
    }
}
