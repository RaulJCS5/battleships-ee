package isel.pdm.ee.battleship.users.author.domain

import isel.pdm.ee.battleship.users.author.ui.LocalAuthorDto


/**
 * The domain entity for representing authors
 */
data class Author(
    val name: String,
    val number: Int,
    val email: String,
    val username: String,
    val github_http: String,
    val linkedin_http: String,
    val ic_author: Int
) {
    init {
        require(username.isNotBlank() && email.isNotBlank())
    }
}

/**
 * Creates a [Author] instance from the given DTO
 */
fun Author(localDto: LocalAuthorDto): Author {
    return Author(
        name = localDto.name,
        number = localDto.number,
        email = localDto.email,
        username = localDto.username,
        github_http = localDto.github_http,
        linkedin_http = localDto.linkedin_http,
        ic_author = localDto.ic_author
    )
}


/**
 * Converts this author to a local DTO, that can be placed in Bundles and
 * passed around between activities.
 */
fun Author.toLocalAuthorDto() =
    LocalAuthorDto(name, number, email, username, github_http, linkedin_http, ic_author)
