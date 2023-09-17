package isel.pdm.ee.battleship.game.ui
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.Game
import isel.pdm.ee.battleship.game.domain.Ship
import isel.pdm.ee.battleship.lobby.ui.mutableListShipPreview
import isel.pdm.ee.battleship.lobby.ui.stringifyShipsLayout
import isel.pdm.ee.battleship.ui.TopBar
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme

internal const val GameScreenTimerTag = "GameScreenTimer"
internal const val GameScreenTag = "GameScreen"
internal const val GameScreenTitleTag = "GameScreenTitle"
internal const val QuitGameButtonTag = "QuitGameButton"

data class GameScreenState(
    @StringRes val title: Int?,
    val game: Game,
    val localShipsLayout: MutableList<Ship>? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    state: GameScreenState,
    onMoveRequested: (Coordinate) -> Unit = { },
    onQuitGameRequested: () -> Unit = { },
    remainingTime: Int,
    timeLimit: Int,
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
                Spacer(modifier = Modifier.height(32.dp))
                val titleTextId = when {
                    state.title != null -> state.title
                    state.game.localPlayerMarker == state.game.board.turn ->
                        R.string.game_screen_your_turn
                    else -> R.string.game_screen_opponent_turn
                }
                Text(
                    text = stringResource(id = titleTextId),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag(GameScreenTitleTag)
                )
                Text(
                    text = stringifyShipsLayout(state.localShipsLayout),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag(GameScreenTitleTag)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row {
                    Text(
                        text = "${stringResource(id = R.string.timer)} ",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.testTag(GameScreenTimerTag)
                    )
                    val remainingTimeLimit = timeLimit - remainingTime
                    Text(
                        text = String.format("%02d:%02d", remainingTimeLimit / 60, remainingTimeLimit % 60),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.testTag(GameScreenTimerTag)
                    )
                }
                BoardView(
                    board = state.game.board,
                    onTileSelected = onMoveRequested,
                    enabled = state.game.localPlayerMarker == state.game.board.turn,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1.0f, true)
                        .fillMaxSize(),
                    fleetOrGame = false
                )
                Button(
                    onClick = onQuitGameRequested,
                    modifier = Modifier.testTag(QuitGameButtonTag)
                ) {
                    Text(
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.background,
                        text = stringResource(id = R.string.game_screen_quit_game))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefineFleetPreview() {
    GameScreen(
        state = GameScreenState(
            title = R.string.game_screen_your_turn,
            game = Game(),
            localShipsLayout = mutableListShipPreview
        ),
        remainingTime = 115,
        timeLimit = 120
    )
}
