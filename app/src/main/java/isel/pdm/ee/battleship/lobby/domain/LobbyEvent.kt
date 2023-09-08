package isel.pdm.ee.battleship.lobby.domain

/**
 * Sum type used to describe events occurring while the player is in the lobby.
 *
 * [PlayersInLobbyUpdate] to describe changes in the set of players in the lobby
 */
sealed class LobbyEvent
data class PlayersInLobbyUpdate(val players: List<PlayerInfo>) : LobbyEvent()

