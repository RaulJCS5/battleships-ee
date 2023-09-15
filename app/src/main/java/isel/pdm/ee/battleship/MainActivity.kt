package isel.pdm.ee.battleship

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.ee.battleship.lobby.ui.LobbyActivity
import isel.pdm.ee.battleship.preferences.ui.PreferencesActivity
import isel.pdm.ee.battleship.users.listAuthors.AuthorsListActivity
import isel.pdm.ee.battleship.users.ranking.RakingListActivity
import isel.pdm.ee.battleship.utils.NavigateAux

class MainActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).userInfoRepo
    }

    companion object {
        //This is for login and register navigate from login to main with success login
        const val TOKEN_EXTRA =
            "TOKEN_EXTRA"

        //From a list of players select one and go for his information
        const val PLAYER_EXTRA =
            "PLAYER_EXTRA"

        //From a list of authors select one and go for his information
        const val AUTHOR_EXTRA =
            "AUTHOR_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                onStartRequested = ::startGame,
                onLeaderBoardRequested = { NavigateAux.navigateTo<RakingListActivity>(this) },
                onAuthorRequested = { NavigateAux.navigateTo<AuthorsListActivity>(this) },
                )
        }
    }

    private fun startGame() {
        if (repo.userInfo != null) {
            LobbyActivity.navigate(this)
        } else
            PreferencesActivity.navigate(this)
    }
}