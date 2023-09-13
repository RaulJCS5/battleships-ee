package isel.pdm.ee.battleship.lobby.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.game.ui.GameActivity
import isel.pdm.ee.battleship.preferences.ui.PreferencesActivity
import isel.pdm.ee.battleship.utils.viewModelInit
import kotlinx.coroutines.launch

/**
 * The screen used to display the list of players in the lobby, that is, available to play.
 */
class LobbyActivity : ComponentActivity() {

    private val viewModel by viewModels<LobbyScreenViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            LobbyScreenViewModel(app.lobbyFirebase, app.userInfoRepo)
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
        setContent {
            val players by viewModel.players.collectAsState()
            LobbyScreen(
                state = LobbyScreenState(players),
                onPlayerSelected = { player -> viewModel.sendMatching(player) },
                onBackRequested = { finish() },
                onPreferencesRequested = {
                    PreferencesActivity.navigate(this, finishOnSave = true)
                }
            )
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.enterLobby()
                try {
                    viewModel.pendingMatching.collect {
                        if (it != null) {
                            Toast.makeText(this@LobbyActivity, "Match found!", Toast.LENGTH_SHORT).show()
                            GameActivity.navigate(
                                origin = this@LobbyActivity
                            )
                        }
                    }
                }
                finally {
                    viewModel.leaveLobby()
                }
            }
        }

    }

}