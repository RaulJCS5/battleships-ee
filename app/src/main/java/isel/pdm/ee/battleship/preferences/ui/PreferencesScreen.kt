package isel.pdm.ee.battleship.preferences.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.preferences.domain.UserInfo
import isel.pdm.ee.battleship.preferences.domain.userInfoOrNull
import isel.pdm.ee.battleship.ui.*
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme
import kotlin.math.min

const val PreferencesScreenTag = "PreferencesScreen"
const val NicknameInputTag = "NicknameInput"
const val MotoInputTag = "MotoInput"

@ExperimentalMaterial3Api
@Composable
fun PreferencesScreen(
    userInfo: UserInfo?,
    onBackRequested: () -> Unit = { },
    onSaveRequested: (UserInfo) -> Unit = { }
) {
    BattleshipTheme {

        var displayedNick by remember { mutableStateOf(userInfo?.nick ?: "") }
        var displayedMoto by remember { mutableStateOf(userInfo?.moto ?: "") }
        var editing by remember { mutableStateOf(userInfo == null) }

        val enteredInfo = userInfoOrNull(
            nick = displayedNick.trim(),
            moto = displayedMoto.trim().ifBlank { null }
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(PreferencesScreenTag),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested),
                    navigationId = R.string.app_name
                ) },
            floatingActionButton = {
                EditFab(
                    onClick =
                    if (!editing) {
                        { editing = true }
                    } else if (enteredInfo == null) null
                    else {
                        { onSaveRequested(enteredInfo) }
                    },
                    mode = if (editing) FabMode.Add else FabMode.Edit
                )
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(
                    text = stringResource(id = R.string.preferences_screen_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(50.dp))

                OutlinedTextField(
                    value = displayedNick,
                    onValueChange = { displayedNick = ensureInputBounds(it) },
                    singleLine = true,
                    label = {
                        Text(
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary,
                            text = stringResource(id = R.string.preferences_screen_nickname_tip))
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Face, contentDescription = "")
                    },
                    readOnly = !editing,
                    modifier = Modifier
                        .padding(horizontal = 48.dp)
                        .fillMaxWidth()
                        .testTag(NicknameInputTag)
                        .semantics { if (!editing) this[IsReadOnly] = Unit }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = displayedMoto,
                    onValueChange = { displayedMoto = ensureInputBounds(it) },
                    maxLines = 3,
                    label = { Text(
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary,
                        text = stringResource(id = R.string.preferences_screen_moto_tip)) },
                    leadingIcon = {
                        Icon(Icons.Default.Info, contentDescription = "")
                    },
                    readOnly = !editing,
                    modifier = Modifier
                        .padding(horizontal = 48.dp)
                        .fillMaxWidth()
                        .testTag(MotoInputTag)
                        .semantics { if (!editing) this[IsReadOnly] = Unit }
                )
                Spacer(
                    modifier = Modifier
                        .sizeIn(minHeight = 128.dp, maxHeight = 256.dp)
                )
            }
        }
    }
}

private const val MAX_INPUT_SIZE = 50
private fun ensureInputBounds(input: String) =
    input.also {
        it.substring(range = 0 until min(it.length, MAX_INPUT_SIZE))
    }

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PreferencesScreenViewModePreview() {
    PreferencesScreen(
        userInfo = UserInfo("my nick", "my moto"),
        onSaveRequested = { }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PreferencesScreenEditModePreview() {
    PreferencesScreen(
        userInfo = null,
        onSaveRequested = { }
    )
}
