package isel.pdm.ee.battleship.game.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.game.domain.BoardResult
import isel.pdm.ee.battleship.game.domain.HasWinner
import isel.pdm.ee.battleship.game.domain.PlayerMarker
import isel.pdm.ee.battleship.game.domain.Tied
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme

const val MatchEndedDialogTag = "MatchEndedDialog"
const val MatchEndedDialogDismissButtonTag = "MatchEndedDialogDismissButton"

@Composable
fun MatchEndedDialog(
    localPLayerMarker: PlayerMarker,
    result: BoardResult,
    onDismissRequested: () -> Unit = { }
) {
    val dialogTextId = when {
        result is Tied -> R.string.match_ended_dialog_text_tied_match
        result is HasWinner && result.winner == localPLayerMarker -> R.string.match_ended_dialog_text_local_won
        else -> R.string.match_ended_dialog_text_opponent_won
    }

    AlertDialog(
        onDismissRequest = onDismissRequested,
        confirmButton = {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                OutlinedButton(
                    border = BorderStroke(0.dp, Color.Unspecified),
                    onClick = onDismissRequested,
                    modifier = Modifier.testTag(MatchEndedDialogDismissButtonTag)
                ) {
                    Text(
                        text = stringResource(id = R.string.match_ended_ok_button),
                        fontSize = 14.sp
                    )
                }
            }
        },
        title = { Text(stringResource(id = R.string.match_ended_dialog_title)) },
        text = { Text(stringResource(id = dialogTextId)) },
        modifier = Modifier.testTag(MatchEndedDialogTag)
    )
}

@Preview(showBackground = true)
@Composable
private fun MatchEndedDialogTiedPreview() {
    BattleshipTheme {
        MatchEndedDialog(
            localPLayerMarker = PlayerMarker.PLAYER1,
            result = Tied()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchEndedDialogLocalWonPreview() {
    BattleshipTheme{
        MatchEndedDialog(
            localPLayerMarker = PlayerMarker.PLAYER1,
            result = HasWinner(winner = PlayerMarker.PLAYER1)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchEndedDialogOpponentWonPreview() {
    BattleshipTheme {
        MatchEndedDialog(
            localPLayerMarker = PlayerMarker.PLAYER1,
            result = HasWinner(winner = PlayerMarker.PLAYER2)
        )
    }
}
