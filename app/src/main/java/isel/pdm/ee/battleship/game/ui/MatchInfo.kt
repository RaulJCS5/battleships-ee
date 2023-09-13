package isel.pdm.ee.battleship.game.ui

import android.os.Parcelable
import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import kotlinx.parcelize.Parcelize

fun MatchInfo(localPlayer: PlayerInfo, matching: Matching):MatchInfoParcelable{
    val opponent = if (localPlayer == matching.player2) matching.player1
    else matching.player2
    return MatchInfoParcelable(
        localPlayerId = localPlayer.id.toString(),
        localPlayerNick = localPlayer.info.nick,
        opponentId = opponent.id.toString(),
        opponentNick = opponent.info.nick,
        matchingId = matching.player1.id.toString(),
    )
}

@Parcelize
data class MatchInfoParcelable(
    val localPlayerId: String,
    val localPlayerNick: String,
    val opponentId: String,
    val opponentNick: String,
    val matchingId: String,
):Parcelable
