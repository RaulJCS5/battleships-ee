package isel.pdm.ee.battleship.users.player

import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.users.model.GameRankTotals
import isel.pdm.ee.battleship.users.model.UserOutputModel

/**
 * Abstraction that characterizes the player.
 */
interface IPlayerService {
    suspend fun fetchRanking(): List<GameRankTotals>
    suspend fun getAllAuthors(): List<Author>
    suspend fun fetchUserMe(tokenAuthorize: String): UserOutputModel
}

class PlayerService() : IPlayerService {
    override suspend fun fetchRanking(): List<GameRankTotals> {
        // TODO: This is a fake implementation, replace with real implementation
        val list = mutableListOf<GameRankTotals>()
        list.add(
            GameRankTotals(UserOutputModel(123,"tomcruise","tomcruise@hotmail.com"),9,7,3)
        )
        list.add(
            GameRankTotals(UserOutputModel(456,"jondavolta","jondavolta@hotmail.com"),9,3,7))
        return list
    }

    //stationary, because is always true
    override suspend fun getAllAuthors(): List<Author> {
        val list = mutableListOf<Author>()
        list.add(
            Author(
                name = "Raul Santos",
                number = 44806,
                email = "a44806@alunos.isel.pt",
                username = "RaulJCS5",
                github_http = "https://github.com/RaulJCS5",
                linkedin_http = "https://www.linkedin.com/in/rauljosecsantos/",
                ic_author = R.drawable.ic_author_rauljcs5
            )
        )
        return list
    }

    override suspend fun fetchUserMe(tokenAuthorize: String): UserOutputModel {
        // TODO: This is a fake implementation, replace with real implementation
        return UserOutputModel(
            id = 1,
            username = "RaulJCS5",
            email = "rauljcs5@gmail.com")
    }
}
