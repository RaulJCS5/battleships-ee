package isel.pdm.ee.battleship.users.ranking.domain

data class UserOutputModel(
    val id: Int,
    val username: String,
    val email: String
)

data class GameRankTotals(
    val user: UserOutputModel,
    val playedGames: Int,
    val winGames: Int,
    val lostGames: Int,
)