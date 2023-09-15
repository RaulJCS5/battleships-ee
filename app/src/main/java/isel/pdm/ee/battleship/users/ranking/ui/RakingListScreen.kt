package isel.pdm.ee.battleship.users.ranking.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.ee.battleship.ui.NavigationHandlers
import isel.pdm.ee.battleship.ui.RefreshingState
import isel.pdm.ee.battleship.ui.TopBar
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme
import isel.pdm.ee.battleship.users.ranking.domain.GameRankTotals
import isel.pdm.ee.battleship.users.ranking.domain.UserOutputModel
import isel.pdm.ee.battleship.utils.NavigatePlayersListScreenTestTag
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.ui.RefreshFab

data class PlayersListScreenState(
    val rankings: List<GameRankTotals> = emptyList(),
    val isLoading: RefreshingState = RefreshingState.Idle
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingListScreen(
    state: PlayersListScreenState = PlayersListScreenState(),
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    onUpdateRequest: () -> Unit = { }
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(NavigatePlayersListScreenTestTag),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopBar(
                    navigation = onNavigationRequested,
                    R.string.app_players_list_screen_title
                )
            },
            floatingActionButton = {
                RefreshFab(
                    onClick = onUpdateRequest,
                    state = state.isLoading,
                    find = R.string.app_find_players_screen_button
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            //isFloatingActionButtonDocked = true
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                items(state.rankings) {
                    ShowPlayerRankView(
                        ranks = it,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RankingListScreenPreview() {
    val rankings = mutableListOf<GameRankTotals>()
    rankings.add(GameRankTotals(UserOutputModel(99,"xpto","xpto@hotmail.com"),9,7,3))
    RankingListScreen(
        state = PlayersListScreenState(
                rankings = rankings
        )
    )
}