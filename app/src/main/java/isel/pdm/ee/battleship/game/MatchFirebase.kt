package isel.pdm.ee.battleship.game

import com.google.firebase.firestore.FirebaseFirestore
import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MatchFirebase(private val db: FirebaseFirestore) : Match {

    private var onGoingGame: Pair<Game, String>? = null


    override fun start(localPlayer: PlayerInfo, matching: Matching): Flow<GameEvent> {
        // fake implementation
        return callbackFlow {
            val game = Game()
            onGoingGame = game to "gameId"
            trySend(GameStarted(game)).isSuccess
            awaitClose()
        }
    }

    override suspend fun makeMove(at: Coordinate) {
        // fake implementation
        val (game, gameId) = onGoingGame!!
        val newGame = game.makeMove(at)
        onGoingGame = newGame to gameId
        db.collection("games").document(gameId).set(newGame).await()
    }

    override suspend fun forfeit() {
        // fake forfeit implementation
        val (game, gameId) = onGoingGame!!
        val newGame = game.copy(forfeitedBy = game.board.turn)
        onGoingGame = newGame to gameId
        db.collection("games").document(gameId).set(newGame).await()
    }

    override suspend fun end() {
        // fake implementation
        onGoingGame = null
    }
}

