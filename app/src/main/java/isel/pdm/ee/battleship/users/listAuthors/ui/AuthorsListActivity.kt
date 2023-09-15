package isel.pdm.ee.battleship.users.listAuthors.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.ee.battleship.DependenciesContainer
import isel.pdm.ee.battleship.main.ui.MainActivity.Companion.AUTHOR_EXTRA
import isel.pdm.ee.battleship.ui.NavigationHandlers
import isel.pdm.ee.battleship.users.author.ui.AuthorActivity
import isel.pdm.ee.battleship.utils.NavigateAux
import isel.pdm.ee.battleship.utils.viewModelInit
import isel.pdm.ee.battleship.users.author.domain.toLocalAuthorDto

/**
 * The activity that hosts the screen for displaying a list of quotes. The
 * list to be displayed is fetched from the API and it bears this week's quotes.
 */
class AuthorsListActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel: AuthorsListScreenViewModel by viewModels {
        viewModelInit {
            AuthorsListScreenViewModel(dependencies.playerService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (viewModel.authors == null)
                viewModel.fetchAllAuthors()
            val authors = viewModel.authors?.getOrNull() ?: emptyList()
            AuthorsListScreen(
                state = AuthorsListScreenState(authors),
                onAuthorSelected = {
                    NavigateAux.navigateTo<AuthorActivity>(
                        ctx = this, AUTHOR_EXTRA,
                        it.toLocalAuthorDto()
                    )
                },
                onNavigationRequested = NavigationHandlers(
                    onBackRequested = { finish() },
                )
            )
        }
    }
}