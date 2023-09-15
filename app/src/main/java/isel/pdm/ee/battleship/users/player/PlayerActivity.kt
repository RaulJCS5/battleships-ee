package isel.pdm.ee.battleship.users.player

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.LocalTokenDto
import isel.pdm.ee.battleship.MainActivity
import isel.pdm.ee.battleship.ui.NavigationHandlers
import isel.pdm.ee.battleship.utils.viewModelInit
import isel.pdm.ee.battleship.ui.RefreshingState

/**
 * Activity that displays the information of a player
 * TODO: This activity is not used in the current version of the app
 */

class PlayerActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }
    private val viewModel: PlayerViewModel by viewModels {
        viewModelInit {
            PlayerViewModel(dependencies.playerService)
        }
    }
    private var fetchFail = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val receivedExtra = tokenExtra

            if (receivedExtra != null && !fetchFail) {
                if (viewModel.user?.getOrNull() == null) {
                    viewModel.fetchUserMe(receivedExtra)
                }
            }
            val loadingState =
                if (viewModel.isLoading) RefreshingState.Refreshing
                else RefreshingState.Idle

            val fetchData = viewModel.user?.getOrNull()
            var player: Player? = null

            if (fetchData != null) {
                if (fetchData.username.isNotBlank() && fetchData.email.isNotBlank()) {
                    player = Player(
                        id = fetchData.id,
                        username = fetchData.username,
                        email = fetchData.email
                    )
                }
            }
            PlayerScreen(
                state = PlayerScreenState(
                    player,
                    loadingState
                ),
                onNavigationRequested = NavigationHandlers(
                    onBackRequested = { finish() }
                ),
            )

        }
    }

    @Suppress("deprecation")
    private val tokenExtra: LocalTokenDto?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(MainActivity.TOKEN_EXTRA, LocalTokenDto::class.java)
            else
                intent.getParcelableExtra(MainActivity.TOKEN_EXTRA)

}