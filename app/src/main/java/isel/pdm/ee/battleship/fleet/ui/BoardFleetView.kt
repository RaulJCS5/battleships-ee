package isel.pdm.ee.battleship.fleet.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import isel.pdm.ee.battleship.game.domain.Board
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.ui.BoardView

@Composable
fun BoardFleetView(
    board: Board,
    onPositionDefineFleet: (Coordinate) -> Unit,
    modifier: Modifier,
) {
    BoardView(
        board = board,
        onTileSelected = {
            onPositionDefineFleet(it)
        },
        enabled = true,
        modifier = modifier,
        fleetOrGame = true
    )
}
