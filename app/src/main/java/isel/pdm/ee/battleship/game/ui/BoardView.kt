package isel.pdm.ee.battleship.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.game.BOARD_SIDE
import isel.pdm.ee.battleship.game.Board
import isel.pdm.ee.battleship.game.Coordinate
import isel.pdm.ee.battleship.game.Game

@Composable
fun BoardView(
    board: Board,
    onTileSelected: (at: Coordinate) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        repeat(BOARD_SIDE) { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(weight = 1.0f, fill = true)
            ) {
                repeat(BOARD_SIDE) { column ->
                    val at = Coordinate(row, column)
                    TileView(
                        move = board[at],
                        enabled = enabled,
                        modifier = Modifier.weight(weight = 1.0f, fill = true),
                        onSelected = { onTileSelected(at) },
                    )
                    if (column != BOARD_SIDE - 1) { VerticalSeparator() }
                }
            }
            if (row != BOARD_SIDE - 1) { HorizontalSeparator() }
        }
    }
}

@Composable
private fun HorizontalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)
        .background(MaterialTheme.colorScheme.background)
    )
}

@Composable
private fun VerticalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxHeight()
        .width(4.dp)
        .background(MaterialTheme.colorScheme.background)
    )
}

@Preview(showBackground = true)
@Composable
private fun BoardViewPreview() {
    val game = Game()
    BoardView(board = game.board, onTileSelected = { }, enabled = true)
}