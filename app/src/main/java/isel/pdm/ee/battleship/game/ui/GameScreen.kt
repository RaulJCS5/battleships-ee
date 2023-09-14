package isel.pdm.ee.battleship.game.ui
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.TAG
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.Game
import isel.pdm.ee.battleship.ui.TopBar
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme

internal const val GameScreenTimerTag = "GameScreenTimer"
internal const val GameScreenTag = "GameScreen"
internal const val GameScreenTitleTag = "GameScreenTitle"
internal const val QuitGameButtonTag = "QuitGameButton"

data class GameScreenState(
    @StringRes val title: Int?,
    val game: Game
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    state: GameScreenState,
    onMoveRequested: (Coordinate) -> Unit = { },
    onQuitGameRequested: () -> Unit = { },
) {
    BattleshipTheme {
        // TODO: This should be a timer that is updated every second
        // Theres a problem in this solution, when the screen is rotated the timer is reset to 120 seconds
        // The timer never starts at 120 seconds
        val remainingTime = rememberSaveable { mutableStateOf(0) }
        if (state.game.localPlayerMarker == state.game.board.turn) {
            DisposableEffect(state.game.board.turn) {
                val timer = object : CountDownTimer(120000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val time = (millisUntilFinished / 1000).toInt()
                        Log.v(TAG, "onTick: $time")
                        remainingTime.value = time
                    }

                    override fun onFinish() {
                        Log.v(TAG, "onFinish")
                        remainingTime.value = 0
                        onQuitGameRequested()
                    }
                }
                timer.start()
                onDispose { timer.cancel() }
            }
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(GameScreenTag),
            topBar = {
                TopBar()
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
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "${stringResource(id = R.string.remaining_time)} ${remainingTime.value}",
                    style = MaterialTheme.typography.displaySmall, // Customize the style as needed
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag(GameScreenTimerTag)
                )
                BoardView(
                    board = state.game.board,
                    onTileSelected = onMoveRequested,
                    enabled = state.game.localPlayerMarker == state.game.board.turn,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1.0f, true)
                        .fillMaxSize()
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
            game = Game()
        )
    )
}
