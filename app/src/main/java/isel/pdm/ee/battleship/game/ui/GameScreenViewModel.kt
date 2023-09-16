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
import isel.pdm.ee.battleship.game.domain.GameEnded
import isel.pdm.ee.battleship.game.domain.GameStarted
import isel.pdm.ee.battleship.game.domain.Match
import isel.pdm.ee.battleship.game.domain.OnGoing
import isel.pdm.ee.battleship.game.domain.TimeEnded
import isel.pdm.ee.battleship.game.domain.TimeUpdated
import isel.pdm.ee.battleship.game.domain.getResult
import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class MatchState { IDLE, STARTING, STARTED, FINISHED }

class GameScreenViewModel(private val match: Match) : ViewModel() {

    val timeLimit: Int = 12 // 2 minutes / 120 seconds

    private val _onGoingGame = MutableStateFlow(Game())
    val onGoingGame = _onGoingGame.asStateFlow()

    private val _remainingTime = MutableStateFlow(0)
    val remainingTime = _remainingTime.asStateFlow()

    private var _state by mutableStateOf(MatchState.IDLE)
    val state: MatchState
        get() = _state

    private var timerJob: Job? = null

    fun makeMove(at: Coordinate): Job? =
        if (state == MatchState.STARTED) {
            viewModelScope.launch {
                match.makeMove(at)
                stopTimer()
                _remainingTime.value = 0
            }
        }
        else {
            Log.v(TAG, "No move")
            null
        }

    fun quitGame(): Job? =
        if (state == MatchState.STARTED)
            viewModelScope.launch {
                match.quitGame()
                stopTimer()
            }
        else {
            Log.v(TAG, "No quit game")
            null
        }

    fun startMatch(localPlayer: PlayerInfo, matching: Matching) {
        if (state == MatchState.IDLE) {
            Log.v(TAG, "startMatch: $state")
            _state = MatchState.STARTING
            viewModelScope.launch {
                // match.startAndObserveGameEvents(localPlayer, matching) subscription moment "I WANT"
                // This executes because I enter the match and I throw a koroutine to start the game
                // .collect{  } moment "HOW TO REACT"
                // This executes because a element arrived to the flow and I want to react to it
                // This runs as many times as things are pushed to the flow
                // This is the moment where I say what to do with the element that arrived from the Flow
                match.startAndObserveGameEvents(localPlayer, matching).collect {
                    _onGoingGame.value = it.game
                    _state = when (it) {
                        is GameStarted -> {
                            Log.v(TAG, "GameStarted 1")
                            MatchState.STARTED
                        }
                        is GameEnded -> {
                            Log.v(TAG, "GameEnded 1")
                            MatchState.FINISHED
                        }
                        else ->
                            if (it.game.getResult() !is OnGoing) {
                                Log.v(TAG, "GameEnded 2")
                                MatchState.FINISHED
                            }
                            else {
                                Log.v(TAG, "GameStarted 2")
                                MatchState.STARTED
                            }
                    }
                    if (_state == MatchState.FINISHED)
                        match.end()
                }
            }
        }
        else {
            Log.v(TAG, "startMatch: $state")
            null
        }
    }

    fun startTimer() {
        if (state == MatchState.STARTED) {
            timerJob = viewModelScope.launch {
                match.startTimer(_remainingTime.value, timeLimit).collect {
                    when(it) {
                        is TimeEnded -> {
                            match.quitGame()
                            stopTimer()
                            //_remainingTime.value = timeLimit
                        }
                        is TimeUpdated -> {
                            _remainingTime.value = it.time
                        }
                        else -> {
                            Log.v(TAG, "No time")
                        }
                    }
                }
            }
        }
        else {
            Log.v(TAG, "No start timer")
        }
    }

    private fun stopTimer() {
        if (timerJob == null) {
            Log.v(TAG, "No timer to stop")
            return
        }
        else {
            //_remainingTime.value = 0
            timerJob?.cancel()
            timerJob = null
            Log.v(TAG, "Timer stopped")
        }
    }
}

