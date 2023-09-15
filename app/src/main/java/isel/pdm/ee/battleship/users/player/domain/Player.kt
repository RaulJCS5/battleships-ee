package isel.pdm.ee.battleship.users.player.domain

import isel.pdm.ee.battleship.users.player.ui.LocalPlayerDto

/**
 * The domain entity for representing players
 */
data class Player(val id: Int, val username: String, val email: String) {
    init {
        require(username.isNotBlank() && email.isNotBlank())
    }
}


/**
 * Creates a [Player] instance from the given DTO
 */
fun Player(localDto: LocalPlayerDto): Player {
    return Player(id = localDto.id, username = localDto.userName, email = localDto.email)
}

/**
 * Converts this user to a local DTO, that can be placed in Bundles and
 * passed around between activities.
 */
fun Player.toLocalPlayerDto() = LocalPlayerDto(id, username, email)
