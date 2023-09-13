package isel.pdm.ee.battleship.lobby.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import isel.pdm.ee.battleship.preferences.domain.UserInfo
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme

const val PlayerInfoScreenTag = "PlayerInfoScreen"

@Composable
fun PlayerInfoScreen(
    playerInfo: PlayerInfo,
    onPlayerSelected: (PlayerInfo) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayerSelected(playerInfo) }
            .testTag(PlayerInfoScreenTag)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = playerInfo.info.nick,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            if (playerInfo.info.moto != null) {
                Text(
                    text = playerInfo.info.moto,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoScreenNoMotoPreview() {
    BattleshipTheme {
        PlayerInfoScreen(
            playerInfo = PlayerInfo(UserInfo("My Nick")),
            onPlayerSelected = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoScreenPreview() {
    BattleshipTheme {
        PlayerInfoScreen(
            playerInfo = PlayerInfo(UserInfo("My Nick", "This is my moto")),
            onPlayerSelected = { }
        )
    }
}
