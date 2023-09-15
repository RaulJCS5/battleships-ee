package isel.pdm.ee.battleship.users.ranking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.ui.NavigationHandlers
import isel.pdm.ee.battleship.ui.RefreshingState
import isel.pdm.ee.battleship.utils.viewModelInit

/**
 * The activity that hosts the screen for displaying a list of ranking players.
 * */
class RakingListActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel: RakingListScreenViewModel by viewModels {
        viewModelInit {
            RakingListScreenViewModel(dependencies.playerService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loadingState =
                if (viewModel.isLoading) RefreshingState.Refreshing
                else RefreshingState.Idle

            val rankList = viewModel.rakingState?.getOrNull() ?: emptyList()
            RankingListScreen(
                state = PlayersListScreenState(rankList, loadingState),
                onUpdateRequest = { viewModel.fetchRanking() },
                onNavigationRequested = NavigationHandlers(
                    onBackRequested = { finish() },
                )
            )
        }
    }
}