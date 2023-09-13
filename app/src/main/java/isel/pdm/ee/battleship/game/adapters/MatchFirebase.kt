package isel.pdm.ee.battleship.game.adapters

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import isel.pdm.ee.battleship.TAG
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.Game
import isel.pdm.ee.battleship.game.domain.GameEvent
import isel.pdm.ee.battleship.game.domain.GameStarted
import isel.pdm.ee.battleship.game.domain.Match
import isel.pdm.ee.battleship.game.domain.makeMove
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
        Log.v(TAG, "start")
        check(onGoingGame == null)
        return callbackFlow {
            Log.v(TAG, "start callbackFlow")
            val game = Game()
            onGoingGame = game to "gameId"
            trySend(GameStarted(game)).isSuccess
            awaitClose()
        }
    }

    override suspend fun makeMove(at: Coordinate) {
        // fake implementation
        //check(onGoingGame != null)
        Log.v(TAG, "makeMove")
        //val (game, gameId) = onGoingGame!!
        //val newGame = game.makeMove(at)
        //onGoingGame = newGame to gameId
        //db.collection("games").document(gameId).set(newGame).await()
    }

    override suspend fun forfeit() {
        // fake forfeit implementation
        //check(onGoingGame != null)
        Log.v(TAG, "forfeit")
        //val (game, gameId) = onGoingGame!!
        //val newGame = game.copy(forfeitedBy = game.board.turn)
        //onGoingGame = newGame to gameId
        //db.collection("games").document(gameId).set(newGame).await()
    }

    override suspend fun end() {
        // fake implementation
        check(onGoingGame != null)
        onGoingGame = null
    }
}

