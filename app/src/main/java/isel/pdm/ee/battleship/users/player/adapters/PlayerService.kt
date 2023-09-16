package isel.pdm.ee.battleship.users.player.adapters

import com.google.firebase.firestore.FirebaseFirestore
import isel.pdm.ee.battleship.R
import isel.pdm.ee.battleship.game.adapters.GAME_ID_LIST_FIELD
import isel.pdm.ee.battleship.game.adapters.SAVE_GAME
import isel.pdm.ee.battleship.game.adapters.WINNER_FIELD
import isel.pdm.ee.battleship.preferences.domain.UserInfo
import isel.pdm.ee.battleship.users.ranking.domain.GameRankTotals
import isel.pdm.ee.battleship.users.ranking.domain.UserOutputModel
import isel.pdm.ee.battleship.users.author.domain.Author
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import kotlin.math.floor

/**
 * Abstraction that characterizes the player.
 */
interface IPlayerService {
    suspend fun fetchRanking(): List<GameRankTotals>
    suspend fun getAllAuthors(): List<Author>
    suspend fun fetchUserMe(tokenAuthorize: String): UserOutputModel
}

class PlayerService(private val db : FirebaseFirestore) : IPlayerService {
    override suspend fun fetchRanking(): List<GameRankTotals> {
        val gameRankTotalsList = mutableListOf<GameRankTotals>()

        // Replace 'yourCollectionName' with the actual name of the collection where player data is stored
        val collectionReference = db.collection(
            SAVE_GAME)

        val querySnapshot = collectionReference.get().await()

        for (documentSnapshot in querySnapshot) {
            val nick = documentSnapshot.id
            val gameIds = documentSnapshot.get(GAME_ID_LIST_FIELD) as? List<String> ?: emptyList()
            val winners = documentSnapshot.get(WINNER_FIELD) as? List<String> ?: emptyList()

            // Calculate the number of played, won, and lost games based on your data
            val playedGames = gameIds.size
            val winGames = winners.size
            val lostGames = playedGames - winGames

            val gameRankTotals = GameRankTotals(UserOutputModel(floor(Math.random()).toInt(),nick,"moto"), playedGames, winGames, lostGames)
            gameRankTotalsList.add(gameRankTotals)
        }

        return gameRankTotalsList
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
