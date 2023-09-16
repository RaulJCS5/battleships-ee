package isel.pdm.ee.battleship.lobby.domain

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction that characterizes the game's lobby.
 */
interface Lobby {
    /**
     * Gets the list of players currently in the lobby
     * @return the list of players currently in the lobby
     */
    suspend fun getPlayers(): List<PlayerInfo>

    /**
     * Enters the lobby. It cannot be entered again until left.
     * @param localPlayer the player that is entering the lobby
     * @return the list of players currently in the lobby
     * @throws IllegalStateException if the lobby was already entered
     */
    suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo>

    /**
     * Leaves the lobby. If the lobby was entered using [enterAndObserve],
     * the returned flow is closed, thereby ending the flow of lobby events.
     * @throws IllegalStateException if the lobby was not yet entered
     */
    suspend fun leave()

    /**
     * Enters the lobby and subscribes to events that occur in the lobby.
     * It cannot be entered again until left.
     * @param localPlayer the player that is entering the lobby
     * @return the flow of lobby events
     * @throws IllegalStateException if the lobby was already entered
     */
    fun enterAndObserve(localPlayer: PlayerInfo): Flow<LobbyEvent>
    /**
     * Creates a matching with the given player. In order to create a matching,
     * the player must have previously entered the lobby. The player that creates
     * the matching exits the lobby.
     * @param [to] the player to which the matching will be issued
     * @return the matching information
     * @throws IllegalStateException if the lobby was not yet entered
     */
    suspend fun createMatching(to: PlayerInfo): Matching
}
