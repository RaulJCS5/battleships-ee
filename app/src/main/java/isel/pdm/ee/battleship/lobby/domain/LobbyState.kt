package isel.pdm.ee.battleship.lobby.domain

import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.channels.ProducerScope

/**
 * Sum type that characterizes the lobby state
 */
sealed class LobbyState

class InUse(
    val localPlayer: PlayerInfo,
    val localPlayerDocRef: DocumentReference? // It is nullable because fakeLobby does not use it
): LobbyState()

class InUseWithFlow(
    val localPlayer: PlayerInfo,
    val scope: ProducerScope<LobbyEvent>
) : LobbyState()

object Idle : LobbyState()
