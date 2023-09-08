package isel.pdm.ee.battleship

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import isel.pdm.ee.battleship.lobby.LobbyActivity
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