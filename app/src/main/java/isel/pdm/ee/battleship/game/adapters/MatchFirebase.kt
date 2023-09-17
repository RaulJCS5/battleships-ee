package isel.pdm.ee.battleship.game.adapters

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import isel.pdm.ee.battleship.TAG
import isel.pdm.ee.battleship.TAG_MODEL
import isel.pdm.ee.battleship.game.domain.BOARD_FIELD
import isel.pdm.ee.battleship.game.domain.Board
import isel.pdm.ee.battleship.game.domain.Coordinate
import isel.pdm.ee.battleship.game.domain.FLEET_FIELD
import isel.pdm.ee.battleship.game.domain.Game
import isel.pdm.ee.battleship.game.domain.GameEnded
import isel.pdm.ee.battleship.game.domain.GameEvent
import isel.pdm.ee.battleship.game.domain.GameStarted
import isel.pdm.ee.battleship.game.domain.Match
import isel.pdm.ee.battleship.game.domain.MoveMade
import isel.pdm.ee.battleship.game.domain.PlayerMarker
import isel.pdm.ee.battleship.game.domain.TURN_FIELD
import isel.pdm.ee.battleship.game.domain.TimeEnded
import isel.pdm.ee.battleship.game.domain.TimeEvent
import isel.pdm.ee.battleship.game.domain.TimeUpdated
import isel.pdm.ee.battleship.game.domain.makeMove
import isel.pdm.ee.battleship.lobby.domain.Matching
import isel.pdm.ee.battleship.lobby.domain.PlayerInfo
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
val PLAYER1: String = "player1"
val PLAYER2: String = "player2"
val QUIT_GAME_FIELD: String = "quitgame"
val GAME_ID: String = "gameid"
val WINNER_FIELD: String = "winner"
val NO_WIN: String = "no win"
val GAME_ID_LIST_FIELD: String = "gameidlist"
val ONGOING: String = "ongoing"
val SAVE_GAME: String = "savegame"
val GAMES_FIELD: String = "games"
var onGoingGame: Pair<Game, String>? = null
/**
 * This class represents the match firebase
 * @property db the firebase firestore
 */
class MatchFirebase(private val db: FirebaseFirestore) : Match {


    /**
     * This function is called when the game is started and is responsible for subscribing to the game state updates.
     * @param localPlayer the local player
     * @param matching the matching
     * @return the flow of game events
     */
    override fun startAndObserveGameEvents(localPlayer: PlayerInfo, matching: Matching): Flow<GameEvent> {
        // fake implementation
        Log.v(TAG, "start")
        check(onGoingGame == null)
        return callbackFlow {
            Log.v(TAG, "start callbackFlow")
            val game = Game(
                localPlayerMarker = getPlayerMarker(localPlayer, matching),
                board = Board()
                )
            val gameId = matching.player1.id.toString()
            var gameSubscription: ListenerRegistration? = null
            try {
                if (localPlayer == matching.player2) {
                    postGameStarted(game, gameId)
                }
                gameSubscription = subscribeGameStateUpdated(
                    localPlayerMarker = game.localPlayerMarker,
                    gameId = gameId,
                    producerScope = this
                )

            } catch (e: Exception) {
                Log.v(TAG, "start callbackFlow exception")
                close(e)
            }
            awaitClose {
                Log.v(TAG, "start callbackFlow awaitClose")
                gameSubscription?.remove()
            }
        }
    }
    /**
     * This function is called when the game is started and is responsible for subscribing to the game state updates.
     * This means that when the game state is updated, the game is updated.
     * @param localPlayerMarker the marker of the local player
     * @param gameId the id of the game
     * @param producerScope the producerScope
     * @return the listenerRegistration
     */
    private fun subscribeGameStateUpdated(
        localPlayerMarker: PlayerMarker,
        gameId: String,
        producerScope: ProducerScope<GameEvent>
    ): ListenerRegistration {
        val ongoingCollection = db.collection(ONGOING).document(gameId)
        val playerMarkerCollection =
            ongoingCollection.collection(localPlayerMarker.name.lowercase())
        val listenerRegistration = playerMarkerCollection.document(BOARD_FIELD)
            .addSnapshotListener { snapshot, error ->
                when {
                    // When theres an error we close the producerScope
                    error != null -> {
                        Log.v(TAG, "Error ${error.message}")
                        producerScope.close(error)
                    }
                    // This snapshot contains the data of the document
                    snapshot != null -> {
                        val snapshotData = snapshot.data
                        // This snapshot contains the data of the document
                        if (snapshotData != null) {
                            Log.v(TAG, "snapshotWithData $snapshotData")
                            val board = snapshotData[BOARD_FIELD] as String
                            val turn = snapshotData[TURN_FIELD] as String
                            val fleet = snapshotData[FLEET_FIELD] as String
                            val quitGamePlayer = snapshotData[QUIT_GAME_FIELD] as String?
                            /*Log.v(TAG_MODEL, "snapshotWithData $board")
                            Log.v(TAG_MODEL, "snapshotWithData $turn")
                            Log.v(TAG_MODEL, "snapshotWithData $fleet")
                            Log.v(TAG_MODEL, "snapshotWithData $quitGamePlayer")*/
                            val playerMarkerTurn = PlayerMarker.valueOf(turn)
                            val realBoard = Board.fromMovesList(
                                valueOf = playerMarkerTurn,
                                moves = board,
                                fleet = fleet
                            )
                            Log.v(TAG, "turn $turn quitGame $quitGamePlayer")
                            val pairGameQuitGame = if (quitGamePlayer != null) {
                                val playerMarkerQuitGame = PlayerMarker.valueOf(quitGamePlayer)
                                Pair(realBoard, playerMarkerQuitGame)
                            } else {
                                Pair(realBoard, null)
                            }
                            val game = Game(
                                localPlayerMarker = localPlayerMarker,
                                quitGameBy = pairGameQuitGame.second,
                                board = pairGameQuitGame.first
                            )
                            val gameEvent = when {
                                onGoingGame == null -> {
                                    Log.v(TAG, "GameStarted")
                                    GameStarted(game)
                                }
                                game.quitGameBy != null -> {
                                    Log.v(TAG, "GameEnded")
                                    // Need to update the other user quitGameBy field
                                    GameEnded(game, game.quitGameBy.other)
                                }
                                else -> {
                                    Log.v(TAG, "MoveMade")
                                    MoveMade(game)
                                }
                            }
                            onGoingGame = Pair(game, gameId )
                            producerScope.trySend(gameEvent)
                        }
                        else {
                            Log.v(TAG, "snapshotWithoutData $snapshotData")
                        }
                    }
                }
            }
        return listenerRegistration
    }
    /**
     * This function is called when the game is started
     * @param game the game that is started
     * @param gameId the id of the game
     */
    private suspend fun postGameStarted(game: Game, gameId: String) {
        try {
            val gameDocRef = db.collection(ONGOING).document(gameId)
            val player1BoardRef = gameDocRef.collection(PLAYER1).document(BOARD_FIELD)
            val player2BoardRef = gameDocRef.collection(PLAYER2).document(BOARD_FIELD)
            player1BoardRef.set(game.board.toDocumentContent()).await()
            player2BoardRef.set(game.board.toDocumentContent()).await()
            println("Game state saved to Firestore successfully.")
        }
        catch (e: Exception) {
            Log.e(TAG, "postGameStarted Exception: ${e.message}", e)
        }
    }

    /**
     * This function is called when the game is started
     * @param localPlayer the local player
     * @param matching the matching
     * @return the player marker
     */
    private fun getPlayerMarker(localPlayer: PlayerInfo, matching: Matching): PlayerMarker {
        return if (localPlayer == matching.player1) PlayerMarker.firstToMove
        else PlayerMarker.firstToMove.other
    }

    /**
     * This function is called when the player makes a move
     * @param at the coordinate of the move
     */
    override suspend fun makeMove(at: Coordinate) {
        onGoingGame = checkNotNull(onGoingGame).also {
            Log.v(TAG, "makeMove")
            val game = it.copy(first = it.first.makeMove(at))
            Log.v(TAG, "makeMove game $game")
            updateGame(game.first, game.second)
        }
    }
    /**
     * This function is called when the timer is started
     * @param time the time
     * @param timeLimit the time limit
     * @return the flow of time events
     */
    override fun startTimer(time: Int, timeLimit: Int): Flow<TimeEvent> {
        return callbackFlow {
            try {
                delay(1000)
                val timer = time + 1
                if (timer>timeLimit){
                    Log.v(TAG, "TimeEnded $timer")
                    val timeEvent = TimeEnded(timer)
                    trySend(timeEvent)
                }
                else{
                    Log.v(TAG, "TimeUpdated $timer")
                    val timeEvent = TimeUpdated(timer)
                    trySend(timeEvent)
                }
            } catch (e: Exception) {
                Log.v(TAG, "startTimer callbackFlow exception $e")
                close(e)
            }
            awaitClose {
                Log.v(TAG, "startTimer callbackFlow awaitClose")
            }
        }
    }

    /**
     * This function is called when the player updates the game
     * @param game the game
     * @param gameId the id of the game
     */
    private suspend fun updateGame(game: Game, gameId: String) {
        try {
            Log.v(TAG, "updateGame $gameId")
            val ongoingDocRef = db.collection(ONGOING).document(gameId)
            val playerMarkerCollection = ongoingDocRef.collection(game.localPlayerMarker.name.lowercase())
            val boardRef = playerMarkerCollection.document(BOARD_FIELD)
            boardRef.update(game.board.toDocumentContent()).await()
            val otherPlayerMarker = game.localPlayerMarker.other.name
            val otherPlayerMarkerCollection = ongoingDocRef.collection(otherPlayerMarker.lowercase())
            val otherBoardRef = otherPlayerMarkerCollection.document(BOARD_FIELD)
            otherBoardRef.update(TURN_FIELD, otherPlayerMarker).await()
            /*val docRef = otherBoardRef.get().await()
            Log.v(TAG_MODEL, "updateGame docRef $docRef")*/
        } catch (e: Exception) {
            Log.e(TAG, "updateGame Exception: ${e.message}", e)
        }
    }

    /**
     * This function is called when the player quits the game
     * TODO: check if the other player is notified
     * TODO: In consequence of the player not being notified the other player can still make moves
     * What is happening?
     * TODO: Is that as soon as the the otherPlayerMarkerCollection is updated with quitGameBy corresponding to the localPlayerMarker and the game is ended,
     * TODO: the playerMarkerCollection cant be notified and update the quitGameBy because the game already ended.
     */
    override suspend fun quitGame() {
        val currentOnGoingGame = onGoingGame
        if (currentOnGoingGame != null) {
            try {
                val game = currentOnGoingGame.first
                val ongoingDocRef = db.collection(ONGOING).document(currentOnGoingGame.second)
                val otherPlayerMarker = game.localPlayerMarker.other.name
                val otherPlayerMarkerCollection =
                    ongoingDocRef.collection(otherPlayerMarker.lowercase())
                val otherBoardRef = otherPlayerMarkerCollection.document(BOARD_FIELD)
                otherBoardRef.update(QUIT_GAME_FIELD, game.localPlayerMarker).await()
                val playerMarkerCollection =
                    ongoingDocRef.collection(game.localPlayerMarker.name.lowercase())
                val boardRef = playerMarkerCollection.document(BOARD_FIELD)
                boardRef.update(QUIT_GAME_FIELD, game.localPlayerMarker).await()
            } catch (e: Exception) {
                Log.e(TAG, "quitGame Exception: ${e.message}", e)
                // TODO: stopTimer() in case it gets stuck here
            }
        } else {
            Log.v(TAG, "quitGame currentOnGoingGame is null")
        }
    }
    /**
     * This function is called when the game is ended
     */
    override suspend fun end() {
        check(onGoingGame != null)
        Log.v(TAG, "end")
        try {
            //saveGame()
            /*val gameDocRef = db.collection(ONGOING).document(onGoingGame!!.second)
            val player1BoardRef = gameDocRef.collection(PLAYER1).document(BOARD_FIELD)
            val player2BoardRef = gameDocRef.collection(PLAYER2).document(BOARD_FIELD)
            player1BoardRef.delete().await()
            player2BoardRef.delete().await()*/
            onGoingGame = null
        }catch (e: Exception){
            Log.e(TAG, "end Exception: ${e.message}", e)
        }
    }

    override suspend fun saveGameAndUpdate(
        localPlayer: PlayerInfo,
        resultWinner: PlayerMarker,
        localPlayerMarker: PlayerMarker
    ) {
        //Log.v(TAG_MODEL, "resultWinner: $resultWinner localPlayerMarker: $localPlayerMarker")
        val gameId = onGoingGame!!.second
        val gameDocRef = db.collection(SAVE_GAME).document(localPlayer.info.nick)
        val isWinner = resultWinner == localPlayerMarker
        val playerWinner = if(isWinner) resultWinner.name else NO_WIN
        // Check if the user already exists
        val userDocSnapshot = gameDocRef.get().await()

        if (userDocSnapshot.exists()) {
            // User exists, update their existing games
            val existingGames = (userDocSnapshot[GAME_ID_LIST_FIELD] as? List<HashMap<String, String>> ?: emptyList()).toMutableList()

            existingGames += hashMapOf(GAME_ID to gameId, WINNER_FIELD to playerWinner) // Add the new game to the list of existing games

            // Update the user document with the new game and winner field
            gameDocRef.update(
                mapOf(
                    GAME_ID_LIST_FIELD to existingGames
                )
            ).await()
        } else {
            // User doesn't exist, create a new user document
            gameDocRef.set(
                mapOf(
                    GAME_ID_LIST_FIELD to listOf(hashMapOf(GAME_ID to gameId, WINNER_FIELD to playerWinner)) // Initialize with the new game
                )
            ).await()
        }
/*
        // Set the game data
        gamesCollection.set(
            hashMapOf(
                WINNER_FIELD to resultWinner,
                GAME_ID to gameId
            )
        ).await()*/
    }


}

