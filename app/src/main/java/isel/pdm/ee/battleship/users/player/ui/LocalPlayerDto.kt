package isel.pdm.ee.battleship.users.player.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Used to create local (to the Android device) representations.
 */
@Parcelize
data class LocalTokenDto(
    val token: String?,
    val userId: Int?
) : Parcelable

@Parcelize
data class LocalPlayerDto(
    val id: Int,
    val userName: String,
    val email: String
) : Parcelable
