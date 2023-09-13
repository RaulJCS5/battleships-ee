package isel.pdm.ee.battleship

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.ee.battleship.lobby.ui.LobbyActivity
import isel.pdm.ee.battleship.preferences.ui.PreferencesActivity

class MainActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).userInfoRepo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(onStartRequested = ::startGame)
        }
    }

    private fun startGame() {
        if (repo.userInfo != null) {
            LobbyActivity.navigate(this)
        } else
            PreferencesActivity.navigate(this)
    }
}