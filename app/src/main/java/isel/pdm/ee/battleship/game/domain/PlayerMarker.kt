package isel.pdm.ee.battleship.game.domain

/**
 * Enumeration type used to represent the game's players markers.
 */
enum class PlayerMarker {

    PLAYER1, PLAYER2;

    companion object {
        val firstToMove: PlayerMarker = PLAYER1
    }

    val other: PlayerMarker
        get() = if (this == PLAYER1) PLAYER2 else PLAYER1
}

