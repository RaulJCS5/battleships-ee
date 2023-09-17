package isel.pdm.ee.battleship.lobby.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.TAG_MODEL
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.Ship
import isel.pdm.ee.battleship.game.domain.ShipType
import isel.pdm.ee.battleship.game.ui.GameActivity
import isel.pdm.ee.battleship.game.ui.GameActivity.Companion.FLEET_INFO_EXTRA
import isel.pdm.ee.battleship.preferences.ui.PreferencesActivity
import isel.pdm.ee.battleship.utils.viewModelInit
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

/**
 * The screen used to display the list of players in the lobby, that is, available to play.
 */
class LobbyActivity : ComponentActivity() {

    private val viewModel by viewModels<LobbyScreenViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            LobbyScreenViewModel(app.lobbyFirebase, app.userInfoRepo)
        }
    }

    companion object {
        fun navigate(origin: Context, mutableList: MutableList<Ship>) {
            with(origin) {
                startActivity(
                    Intent(this, LobbyActivity::class.java).also {
                        it.putExtra(FLEET_INFO_EXTRA, FleetInfo(mutableList))
                    }
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val players by viewModel.players.collectAsState()
            LobbyScreen(
                state = LobbyScreenState(players),
                onPlayerSelected = { player -> viewModel.sendMatching(player) },
                onBackRequested = { finish() },
                onPreferencesRequested = {
                    PreferencesActivity.navigate(this, finishOnSave = true)
                }
            )
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.enterLobby()
                try {
                    viewModel.pendingMatching.collect {
                        if (it != null) {
                            Toast.makeText(this@LobbyActivity, "Match found!", Toast.LENGTH_SHORT).show()
                            GameActivity.navigate(
                                origin = this@LobbyActivity,
                                localPlayer = it.localPlayer,
                                matching = it.matching,
                                mutableListShip = mutableListShip
                            )
                        }
                    }
                }
                finally {
                    viewModel.leaveLobby()
                }
            }
        }

    }

    private val fleetInfoParcelable: FleetInfoParcelable by lazy {
        val info =
            intent.getParcelableExtra(FLEET_INFO_EXTRA, FleetInfoParcelable::class.java)

        checkNotNull(info)
    }

    private val mutableListShip : MutableList<Ship> by lazy {
        convertFleetInfoParcelableToMutableListShip(fleetInfoParcelable)
    }

}

fun convertFleetInfoParcelableToMutableListShip(fleetInfoParcelable: FleetInfoParcelable): MutableList<Ship> =
    fleetInfoParcelable.localShipDtoMutableList?.map {
        Ship(
            shipType = ShipType.valueOf(it.shipType.nameType),
            coordinate = it.coordinate?.let { coordinate ->
                Coordinate(
                    row = coordinate.row,
                    column = coordinate.column
                )
            },
            orientation = it.orientation,
            isSunken = it.isSunken
        )
    }?.toMutableList() ?: mutableListOf()

@Parcelize
data class FleetInfoParcelable(
    val localShipDtoMutableList: MutableList<LocalShipDto>?=null
) : Parcelable

fun FleetInfo(shipMutableList: MutableList<Ship>): FleetInfoParcelable {
    return FleetInfoParcelable(
        localShipDtoMutableList = shipMutableList.map {
            LocalShipDto(
                shipType = LocalShipTypeDto.valueOf(it.shipType.name),
                coordinate = it.coordinate?.let { coordinate ->
                    LocalCoordinateDto(
                        row = coordinate.row,
                        column = coordinate.column
                    )
                },
                orientation = it.orientation,
                isSunken = it.isSunken
            )
        }.toMutableList()
    )
}

@Parcelize
data class LocalShipDto(
    val shipType: LocalShipTypeDto,
    val coordinate: LocalCoordinateDto?,
    val orientation: String,
    val isSunken: Boolean = false
) : Parcelable

@Parcelize
data class LocalCoordinateDto(val row: Int, val column: Int) : Parcelable

@Parcelize
enum class LocalShipTypeDto(val size: Int, val nameType: String): Parcelable {
    CARRIER(5, "CARRIER"),
    BATTLESHIP(4, "BATTLESHIP"),
    SUBMARINE(3, "SUBMARINE"),
    CRUISER(3, "CRUISER"),
    DESTROYER(2, "DESTROYER");
}