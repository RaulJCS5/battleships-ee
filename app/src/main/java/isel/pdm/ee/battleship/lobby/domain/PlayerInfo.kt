package isel.pdm.ee.battleship.lobby.domain

import isel.pdm.ee.battleship.preferences.domain.UserInfo
import java.util.*

/**
 * Data type that characterizes the player information while he's in the lobby
 * @property info The information entered by the user
 * @property id An identifier used to distinguish players in the lobby
 */
data class PlayerInfo(val info: UserInfo, val id: UUID = UUID.randomUUID())
