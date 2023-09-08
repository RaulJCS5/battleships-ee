package isel.pdm.ee.battleship.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.ee.battleship.lobby.domain.Lobby
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import isel.pdm.ee.battleship.lobby.domain.PlayersInLobbyUpdate
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
    private var localPlayer: PlayerInfo? = null

    /**
     * Enters the lobby.
     * create a coroutine that will monitor the lobby and update the list of players
     * the flow terminates when the lobby is left
     * when theres a change in the lobby, the list of players is updated
     * the list of players is filtered to remove the local player
     * @return the job that monitors the lobby
     */
    fun enterLobby(): Job? {
        if (lobbyMonitor == null) {
            val localPlayerUpdate = PlayerInfo(checkNotNull(userInfoRepo.userInfo))
            lobbyMonitor = viewModelScope.launch {
                lobby.enterAndObserve(localPlayerUpdate).collect { event ->
                    when (event) {
                        is PlayersInLobbyUpdate -> {
                            _players.value = event.players.filter {
                                // TODO: For demonstration purposes, we want to see our own player in the list
                                //it != localPlayer
                                true
                            }
                        }

                        else -> {

                        }
                    }
                }
            }
            localPlayer = localPlayerUpdate
            return lobbyMonitor
        } else {
            return null
        }
    }

    /**
     * Leaves the lobby.
     */
    fun leaveLobby(): Job? {
        return if (lobbyMonitor != null) {
            viewModelScope.launch {
                lobbyMonitor?.cancel()
                lobbyMonitor = null
                lobby.leave()
            }
            lobbyMonitor
        } else {
            null
        }
    }
}
