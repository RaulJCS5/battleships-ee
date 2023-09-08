package isel.pdm.ee.battleship.lobby.domain

import kotlinx.coroutines.channels.ProducerScope

/**
 * Sum type that characterizes the lobby state
 */
sealed class LobbyState

class InUse(
    val localPlayer: PlayerInfo,
): LobbyState()

class InUseWithFlow(
    val localPlayer: PlayerInfo,
    val scope: ProducerScope<LobbyEvent>
) : LobbyState()

object Idle : LobbyState()
