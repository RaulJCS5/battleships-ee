package isel.pdm.ee.battleship

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.ee.battleship.game.ui.ForfeitButtonTag
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme

const val MainScreenTag = "MainScreen"
const val PlayButtonTag = "PlayButton"

@Composable
fun MainScreen(onStartRequested: () -> Unit = { }) {
    BattleshipTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag(MainScreenTag),
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = onStartRequested,
                    modifier = Modifier.testTag(PlayButtonTag)
                ) {
                    Text(
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.background,
                        text = stringResource(id = R.string.start_game_button_text)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}