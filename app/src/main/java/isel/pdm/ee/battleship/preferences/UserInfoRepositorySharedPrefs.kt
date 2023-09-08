package isel.pdm.ee.battleship.preferences

import android.content.Context
import isel.pdm.ee.battleship.preferences.domain.UserInfo
import isel.pdm.ee.battleship.preferences.domain.UserInfoRepository

/**
 * A user information repository implementation supported in shared preferences
 */
class UserInfoRepositorySharedPrefs(private val context: Context): UserInfoRepository {

    private val userNickKey = "Nick"
    private val userMotoKey = "Moto"

    private val prefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }

    override var userInfo: UserInfo?
        get() {
            val savedNick = prefs.getString(userNickKey, null)
            return if (savedNick != null)
                UserInfo(savedNick, prefs.getString(userMotoKey, null))
            else
                null
        }
        /**
         * Suggests delimitation of a set of operations that are carried out as a whole or not
         * edit() edit() change operations and at the end apply the changes made to this object in the store
         * apply() vs commit()
         * apply() to be main friendly needs to be asynchronous
         * */
        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(userNickKey)
                    .remove(userMotoKey)
                    .apply()
            else
                prefs.edit()
                    .putString(userNickKey, value.nick)
                    .putString(userMotoKey, value.moto)
                    .apply()
        }
}