package isel.pdm.ee.battleship.game.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.ee.battleship.TAG
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.Game
import isel.pdm.ee.battleship.game.domain.Match
import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class MatchState { IDLE, STARTING, STARTED, FINISHED }

class GameScreenViewModel(private val match: Match) : ViewModel() {

    private val _onGoingGame = MutableStateFlow(Game())
    val onGoingGame = _onGoingGame.asStateFlow()

    private var _state by mutableStateOf(MatchState.IDLE)
    val state: MatchState
        get() = _state


    fun makeMove(at: Coordinate): Job? =
        if (state == MatchState.STARTED) {
            viewModelScope.launch {
                match.makeMove(at)
            }
        }
        else {
            Log.v(TAG, "No move")
            null
        }

    fun forfeit(): Job? =
        if (state == MatchState.STARTED) viewModelScope.launch {
            match.forfeit()
        }
        else {
            Log.v(TAG, "No forfeit")
            null
        }

    fun startMatch(localPlayer: PlayerInfo, matching: Matching) {
        if (state == MatchState.IDLE) {
            Log.v(TAG, "startMatch: $state")
            _state = MatchState.STARTING
            viewModelScope.launch {
                match.start(localPlayer, matching)
                _state = MatchState.STARTED
            }
        }
        else {
            Log.v(TAG, "startMatch: $state")
        }
    }
}

