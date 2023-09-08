package isel.pdm.ee.battleship.lobby

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.ee.battleship.TAG
import isel.pdm.ee.battleship.preferences.domain.UserInfoRepository
import kotlinx.coroutines.Job
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
     * collects the list of players in the lobby and updates the [_players] flow.
     * if appears a new element in the list of players, it will be added to the [_players] flow.
     * each time appears a new element in the list of players, it will be added to the [_players] flow.
     * publishing in a MutableStateFlow will trigger a recomposition of the UI.
     */
    fun enterLobby(){
        if(lobbyMonitor == null) {
            lobbyMonitor = viewModelScope.launch {
                lobby.players.collect { players ->
                    _players.value = players
                    Log.v(TAG, "LobbyScreenViewModel: enterLobby: players: ${players.size}")
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
