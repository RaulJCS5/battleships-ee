package isel.pdm.ee.battleship.game.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.R

import isel.pdm.ee.battleship.utils.viewModelInit

class GameActivity: ComponentActivity() {

    private val viewModel by viewModels<GameScreenViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            GameScreenViewModel(app.match)
        }
    }

    companion object {
        fun navigate(origin: Context) {
            with(origin) {
                startActivity(
                    Intent(this, GameActivity::class.java)
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentGame by viewModel.onGoingGame.collectAsState()
            val currentState = viewModel.state
            val title = when (currentState) {
                MatchState.STARTING -> R.string.game_screen_waiting
                MatchState.IDLE -> R.string.game_screen_waiting
                else -> R.string.game_screen_waiting
            }
            GameScreen(
                state = GameScreenState(title, currentGame),
                onMoveRequested = { at -> viewModel.makeMove(at) },
                onForfeitRequested = { viewModel.forfeit() }
            )
        }
    }
}