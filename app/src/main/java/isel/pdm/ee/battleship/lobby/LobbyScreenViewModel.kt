package isel.pdm.ee.battleship.lobby

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.ee.battleship.TAG
import isel.pdm.ee.battleship.preferences.domain.UserInfoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * View model for the Lobby Screen hosted by [LobbyActivity].
 */
class LobbyScreenViewModel(
    val lobby: Lobby,
    val userInfoRepo: UserInfoRepository,
) : ViewModel() {

    private val _players = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val players = _players.asStateFlow()

    private var lobbyMonitor: Job? = null

    /**
     * Enters the lobby.
     */
    fun enterLobby(){
        if(lobbyMonitor == null) {
            lobbyMonitor = viewModelScope.launch {
                while (true) {
                    _players.value = lobby.getPlayers()
                    Log.v(TAG, "Players: ${_players.value}")
                    delay(5000)
                }
            }
        }
    }
    /**
     * Leaves the lobby.
     */
    fun leaveLobby(){
        lobbyMonitor?.cancel()
        lobbyMonitor = null
    }
}
