package isel.pdm.ee.battleship.users.author

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.ui.NavigationHandlers
import isel.pdm.ee.battleship.ui.TopBar
import isel.pdm.ee.battleship.ui.theme.BattleshipTheme
import isel.pdm.ee.battleship.users.player.Author
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.utils.NavigateAuthorTestTag

data class AuthorScreenState(
    val author: Author? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorScreen(
    state: AuthorScreenState = AuthorScreenState(),
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    onSendEmailRequested: () -> Unit = { },
    onOpenUrlRequested: (Uri) -> Unit = { }
) {
    BattleshipTheme() {
        Scaffold(
            topBar = {
                TopBar(onNavigationRequested, R.string.app_author_screen_title) },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                if (state.author != null) {
                    UsersView(
                        username = state.author.username,
                        email = state.author.email,
                        ic = state.author.ic_author,
                        modifier = Modifier
                            .padding(16.dp)
                            .testTag(NavigateAuthorTestTag)
                            .clickable { onSendEmailRequested() }
                    )
                    val socialLinks = listOf(
                        SocialInfo(
                            link = Uri.parse(state.author.github_http),
                            imageId = R.drawable.ic_github
                        ),
                        SocialInfo(
                            link = Uri.parse(state.author.linkedin_http),
                            imageId = R.drawable.ic_linkedin
                        )
                    )
                    Socials(
                        socials = socialLinks,
                        onOpenUrlRequested = onOpenUrlRequested
                    )
                }
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.fillMaxWidth()
                ) {
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorPreview() {
    val state = AuthorScreenState(
        Author(
            name = "Unknown1",
            number = 99999,
            email = "Unknown1@alunos.isel.pt",
            username = "Unknown1",
            github_http = "https://github.com/Unknown1",
            linkedin_http = "https://www.linkedin.com/in/unknown1/",
            ic_author = R.drawable.clipart3240117
        )
    )
    AuthorScreen(
        state = state
    )
}