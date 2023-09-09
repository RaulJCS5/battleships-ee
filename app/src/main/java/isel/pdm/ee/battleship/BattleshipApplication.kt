package isel.pdm.ee.battleship

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import isel.pdm.ee.battleship.lobby.FakeLobby
import isel.pdm.ee.battleship.lobby.LobbyFirebase
import isel.pdm.ee.battleship.lobby.domain.Lobby
import isel.pdm.ee.battleship.preferences.UserInfoRepositorySharedPrefs
import isel.pdm.ee.battleship.preferences.domain.UserInfoRepository


const val TAG = "BattleshipApp"

/**
 * The contract for the object that holds all the globally relevant dependencies.
 */
interface DependenciesContainer {
    val userInfoRepo: UserInfoRepository
    val fakeLobby: Lobby
    val lobbyFirebase: Lobby
}

/**
 * The application class to be used as a Service Locator.
 */
class BattleshipApplication : DependenciesContainer, Application() {

    // 10.0.2.2 is the special IP address to connect to the 'localhost' of
    // the host computer from an Android emulator.
    private val emulatedFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.2.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
        }
    }
    private val realFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    override val userInfoRepo: UserInfoRepository
        get() = UserInfoRepositorySharedPrefs(this)

    override val fakeLobby: Lobby
        get() = FakeLobby()

    override val lobbyFirebase: Lobby
        get() = LobbyFirebase(emulatedFirestoreDb)

}
