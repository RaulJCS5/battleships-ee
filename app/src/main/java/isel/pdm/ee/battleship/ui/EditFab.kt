package isel.pdm.ee.battleship.ui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme

enum class FabMode { Edit, Add }

const val EditButtonTag = "EditButton"
const val AddButtonTag = "AddButton"

@Composable
fun EditFab(
    onClick: (() -> Unit)? = null,
    mode: FabMode = FabMode.Edit,
) {
    Button(
        onClick = onClick ?: { },
        enabled = if (mode == FabMode.Edit) true else onClick != null,
        shape = CircleShape,
        modifier = Modifier
            .defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
            .testTag(
                tag =
                if (mode == FabMode.Edit) EditButtonTag
                else AddButtonTag
            )
    ){
        if (mode == FabMode.Edit) {
            Icon(Icons.Default.Edit, contentDescription = "")
        } else {
            Icon(Icons.Default.Add, contentDescription = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditFabEditModePreview() {
    BattleshipTheme {
        EditFab(onClick = { }, mode = FabMode.Edit)
    }
}

@Preview(showBackground = true)
@Composable
private fun EditFabUpdateModePreview() {
    BattleshipTheme {
        EditFab(onClick = { }, mode = FabMode.Add)
    }
}

@Preview(showBackground = true)
@Composable
private fun DisabledEditFabUpdateModePreview() {
    BattleshipTheme {
        EditFab(mode = FabMode.Add)
    }
}
