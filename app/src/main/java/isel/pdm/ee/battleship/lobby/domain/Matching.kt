package isel.pdm.ee.battleship.lobby.domain

/**
 * Data type that characterizes matching.
 * @property player1 The player1 information
 * @property player2 The information of the player2
 */
data class Matching(val player1: PlayerInfo, val player2: PlayerInfo)


/**
 * The player information of the first player to move for this matching.
 */
val Matching.firstToMove: PlayerInfo
    get() = player1
