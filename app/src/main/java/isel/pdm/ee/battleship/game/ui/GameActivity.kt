package isel.pdm.ee.battleship.game.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import isel.pdm.ee.battleship.preferences.domain.UserInfo

import isel.pdm.ee.battleship.utils.viewModelInit
import java.util.UUID

class GameActivity: ComponentActivity() {

    private val viewModel by viewModels<GameScreenViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            GameScreenViewModel(app.match)
        }
    }

    companion object {
        const val MATCH_INFO_EXTRA = "MATCH_INFO_EXTRA"
        fun navigate(origin: Context, localPlayer: PlayerInfo, matching: Matching) {
            with(origin) {
                startActivity(
                    Intent(this, GameActivity::class.java).also {
                        it.putExtra(MATCH_INFO_EXTRA, MatchInfo(localPlayer, matching))
                    }
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
                MatchState.STARTING -> R.string.game_screen_starting
                MatchState.IDLE -> R.string.game_screen_idle
                else -> null
            }
            GameScreen(
                state = GameScreenState(title, currentGame),
                onMoveRequested = { at -> viewModel.makeMove(at) },
                onForfeitRequested = { viewModel.forfeit() }
            )
        }
        if (viewModel.state == MatchState.IDLE) {
            viewModel.startMatch(localPlayer, matching)
        }
    }
    private val matchInfoParcelable: MatchInfoParcelable by lazy {
        val info =
            intent.getParcelableExtra(MATCH_INFO_EXTRA, MatchInfoParcelable::class.java)
        checkNotNull(info) { "Missing match info" }
    }
    val localPlayer by lazy {
        PlayerInfo(
            id = UUID.fromString(matchInfoParcelable.localPlayerId),
            info = UserInfo(matchInfoParcelable.localPlayerNick)
        )
    }
    val matching by lazy {
        val opponent = PlayerInfo(
            id = UUID.fromString(matchInfoParcelable.opponentId),
            info = UserInfo(matchInfoParcelable.opponentNick)
        )
        Matching(
            player1 = localPlayer,
            player2 = opponent,
        )
    }

}