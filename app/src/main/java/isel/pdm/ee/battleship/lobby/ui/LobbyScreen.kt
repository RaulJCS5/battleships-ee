package isel.pdm.ee.battleship.lobby.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import isel.pdm.ee.battleship.game.domain.Ship
import isel.pdm.ee.battleship.game.domain.ShipType
import isel.pdm.ee.battleship.game.ui.GameScreenTitleTag
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import isel.pdm.ee.battleship.preferences.domain.UserInfo
import isel.pdm.ee.battleship.ui.NavigationHandlers
import isel.pdm.ee.battleship.ui.TopBar
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme

const val LobbyScreenTag = "LobbyScreen"

data class LobbyScreenState(
    val players: List<PlayerInfo> = emptyList(),
    val localShipsLayout: MutableList<Ship>? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(
    state: LobbyScreenState = LobbyScreenState(),
    onPlayerSelected: (PlayerInfo) -> Unit = { },
    onBackRequested: () -> Unit = { },
    onPreferencesRequested: () -> Unit = { }
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LobbyScreenTag),
            topBar = {
                TopBar(
                    NavigationHandlers(onBackRequested, onPreferencesRequested),
                    R.string.app_name
                )
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.lobby_screen_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringifyShipsLayout(state.localShipsLayout),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag(GameScreenTitleTag)
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(state.players) {
                        PlayerInfoScreen(playerInfo = it, onPlayerSelected)
                    }
                }
            }
        }
    }
}

fun stringifyShipsLayout(localShipsLayout: MutableList<Ship>?): String {
    val result = localShipsLayout?.joinToString("\n") {
        "${it.shipType}: ${it.coordinate}, ${it.orientation}"
    }
    return result.toString()
}

@Preview(showBackground = true)
@Composable
private fun LobbyScreenPreview() {
    LobbyScreen(
        state = LobbyScreenState(
            players,
            mutableListShipPreview
        ),
        onBackRequested = { },
        onPreferencesRequested = { }
    )
}

private val players = buildList {
    repeat(30) {
        add(PlayerInfo(UserInfo("My Nick $it", "This is my $it moto")))
    }
}

val mutableListShipPreview = mutableListOf<Ship>(
    Ship(ShipType.BATTLESHIP, Coordinate(0,0), "R"),
    Ship(ShipType.CARRIER, Coordinate(0,0), "R"),
    Ship(ShipType.CRUISER, Coordinate(0,0), "R"),
    Ship(ShipType.DESTROYER, Coordinate(0,0), "R"),
    Ship(ShipType.SUBMARINE, Coordinate(0,0), "R"),
)