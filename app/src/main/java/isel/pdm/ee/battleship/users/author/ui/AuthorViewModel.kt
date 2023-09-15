package isel.pdm.ee.battleship.users.author.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.pdm.ee.battleship.users.author.domain.Author
import kotlinx.coroutines.launch
import isel.pdm.ee.battleship.users.player.adapters.IPlayerService

class AuthorViewModel(private val authorService: IPlayerService) : ViewModel() {

    private var _author by mutableStateOf<Result<Author>?>(null)
    val author: Result<Author>?
        get() = _author

    fun setAuthors(receivedExtra: LocalAuthorDto) {

        viewModelScope.launch {
            _author =
                try {
                    Result.success(
                        Author(receivedExtra)
                    )
                } catch (e: Exception) {
                    Result.failure(e)
                }
        }
    }
}