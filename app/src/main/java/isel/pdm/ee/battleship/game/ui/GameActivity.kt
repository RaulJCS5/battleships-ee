package isel.pdm.ee.battleship.game.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.TAG_MODEL
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.PlayerMarker
import isel.pdm.ee.battleship.game.domain.Ship
import isel.pdm.ee.battleship.game.domain.ShipType
import isel.pdm.ee.battleship.game.domain.getResult
import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import isel.pdm.ee.battleship.lobby.ui.FleetInfo
import isel.pdm.ee.battleship.lobby.ui.FleetInfoParcelable
import isel.pdm.ee.battleship.lobby.ui.convertFleetInfoParcelableToMutableListShip
import isel.pdm.ee.battleship.preferences.domain.UserInfo

import isel.pdm.ee.battleship.utils.viewModelInit
import java.util.UUID

class GameActivity: ComponentActivity() {

    private val viewModel by viewModels<GameScreenViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            GameScreenViewModel(app.match, app.userInfoRepo)
        }
    }

    companion object {
        const val MATCH_INFO_EXTRA = "MATCH_INFO_EXTRA"
        const val FLEET_INFO_EXTRA = "FLEET_INFO_EXTRA"
        fun navigate(
            origin: Context,
            localPlayer: PlayerInfo,
            matching: Matching,
            mutableListShip: MutableList<Ship>
        ) {
            with(origin) {
                startActivity(
                    Intent(this, GameActivity::class.java).also {
                        it.putExtra(MATCH_INFO_EXTRA, MatchInfo(localPlayer, matching))
                        it.putExtra(FLEET_INFO_EXTRA, FleetInfo(mutableListShip))
                    }
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hashMapShip = viewModel.hashFleetBoard?.getOrNull()
        setContent {
            // This currentGame goes to the viewModel and says Im interested in the currentGame and I want to know when it changes
            // because Im using Flow and MutableStateFlow
            // Flow for progression until the ViewModel
            // MutableStateFlow for the recomposition in the UI when the state changes
            val currentGame by viewModel.onGoingGame.collectAsState()
            val remainingTime by viewModel.remainingTime.collectAsState()
            val currentFleetInfo by viewModel.fleetInfo.collectAsState()
            val timeLimit = viewModel.timeLimit
            val currentState = viewModel.state
            //Log.v(TAG_MODEL, "Fleet info $mutableListShip")
            val title = when (currentState) {
                MatchState.STARTING -> R.string.game_screen_starting
                MatchState.IDLE -> R.string.game_screen_idle
                else -> null
            }
            GameScreen(
                state = GameScreenState(title, currentGame, currentFleetInfo),
                onMoveRequested = { at -> viewModel.makeMove(at) },
                onQuitGameRequested = { viewModel.quitGame() },
                remainingTime = remainingTime,
                timeLimit = timeLimit
            )
            when (currentState) {
                //MatchState.STARTING -> StartingMatchDialog()
                MatchState.FINISHED -> MatchEndedDialog(
                    localPLayerMarker = currentGame.localPlayerMarker,
                    result = currentGame.getResult(),
                    onDismissRequested = { finish() }
                )
                else -> { }
            }
            if (currentGame.localPlayerMarker == currentGame.board.turn) {
                viewModel.startTimer()
            }
        }
        if (viewModel.state == MatchState.IDLE) {
            if (matching.player1==localPlayer) {
                hashMapShip!![PlayerMarker.PLAYER1.name] = mutableListShip
            } else {
                hashMapShip!![PlayerMarker.PLAYER2.name] = mutableListShip
            }
            viewModel.showShipsLayout(mutableListShip)
            viewModel.startMatch(localPlayer, matching)
        }
        onBackPressedDispatcher.addCallback(owner = this, enabled = true) {
            viewModel.quitGame()
            finish()
        }
    }
    private val matchInfoParcelable: MatchInfoParcelable by lazy {
        val info =
            intent.getParcelableExtra(MATCH_INFO_EXTRA, MatchInfoParcelable::class.java)
        checkNotNull(info) { "Missing match info" }
    }
    private val fleetInfoParcelable: FleetInfoParcelable by lazy {
        val info =
            intent.getParcelableExtra(FLEET_INFO_EXTRA, FleetInfoParcelable::class.java)
        checkNotNull(info) { "Missing fleet info" }
    }

    private val mutableListShip : MutableList<Ship> by lazy {
        convertFleetInfoParcelableToMutableListShip(fleetInfoParcelable)
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
        if (localPlayer.id.toString()== matchInfoParcelable.matchingId) {
            Matching(localPlayer, opponent)
        } else {
            Matching(opponent, localPlayer)
        }
    }

}