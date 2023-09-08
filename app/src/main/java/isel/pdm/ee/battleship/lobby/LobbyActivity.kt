package isel.pdm.ee.battleship.lobby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.TAG
import isel.pdm.ee.battleship.preferences.ui.PreferencesActivity
import isel.pdm.ee.battleship.utils.viewModelInit

/**
 * The screen used to display the list of players in the lobby, that is, available to play.
 */
class LobbyActivity : ComponentActivity() {

    private val viewModel by viewModels<LobbyScreenViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            LobbyScreenViewModel(app.lobby, app.userInfoRepo)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, LobbyActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.enterLobby()
        setContent {
            val players by viewModel.players.collectAsState()
            LobbyScreen(
                state = LobbyScreenState(players),
                onPlayerSelected = {  },
                onBackRequested = { finish() },
                onPreferencesRequested = {
                    PreferencesActivity.navigate(this, finishOnSave = true)
                }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart")
        viewModel.enterLobby()
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop")
        viewModel.leaveLobby()
    }
}