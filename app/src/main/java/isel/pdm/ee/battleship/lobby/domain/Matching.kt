package isel.pdm.ee.battleship.lobby.domain

/**
 * Data type that characterizes challenges.
 * @property player1     The challenger information
 * @property player2     The information of the challenged player
 */
data class Matching(val player1: PlayerInfo, val player2: PlayerInfo)


/**
 * The player information of the first player to move for this challenge.
 */
val Matching.firstToMove: PlayerInfo
    get() = player1
