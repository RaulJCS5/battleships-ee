package isel.pdm.ee.battleship.users.author.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalAuthorDto(
    val name: String, val number: Int, val email: String, val username: String,
    val github_http: String, val linkedin_http: String, val ic_author: Int
) : Parcelable
