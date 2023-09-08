package isel.pdm.ee.battleship

import android.app.Application
import isel.pdm.ee.battleship.lobby.FakeLobby
import isel.pdm.ee.battleship.lobby.domain.Lobby
import isel.pdm.ee.battleship.preferences.UserInfoRepositorySharedPrefs
import isel.pdm.ee.battleship.preferences.domain.UserInfoRepository


const val TAG = "BattleshipApp"

/**
 * The contract for the object that holds all the globally relevant dependencies.
 */
interface DependenciesContainer {
    val userInfoRepo: UserInfoRepository
    val lobby: Lobby
}

/**
 * The application class to be used as a Service Locator.
 */
class BattleshipApplication : DependenciesContainer, Application() {

    override val userInfoRepo: UserInfoRepository
        get() = UserInfoRepositorySharedPrefs(this)

    override val lobby: Lobby
        get() = FakeLobby()

}

