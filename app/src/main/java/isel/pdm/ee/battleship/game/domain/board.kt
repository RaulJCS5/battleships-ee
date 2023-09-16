package isel.pdm.ee.battleship.game.domain


const val BOARD_FIELD= "board"
const val TURN_FIELD = "turn"
const val FLEET_FIELD = "fleet"

/**
 * Represents a board. Instances are immutable.
 * @property turn The next player to move
 * @property tiles The board tiles
 * @property ships The ships in the board
 */
data class Board(
    val turn: PlayerMarker = PlayerMarker.firstToMove,
    val tiles: MutableList<MutableList<PositionStateBoard>> =
        MutableList(
            size = BOARD_SIDE,
            init = { row->
                MutableList(size = BOARD_SIDE, init = { col ->
                    PositionStateBoard(
                        boardPosition = Coordinate(row, col),
                        wasShoot = false, wasShip = false, shipType = null, shipLayout = null
                    )
                }) }
        ),
    val ships: MutableList<Ship> = mutableListOf(),
) {
    /**
     * Fake constructor for testing purposes
     * TODO: Remove this constructor
     */
    init {
        ships.add(Ship(ShipType.CARRIER, Coordinate(0,0), "D"))
        ships.add(Ship(ShipType.BATTLESHIP, Coordinate(0,1), "D"))
        ships.add(Ship(ShipType.CRUISER, Coordinate(0,2), "D"))
        ships.add(Ship(ShipType.SUBMARINE, Coordinate(0,3), "D"))
        ships.add(Ship(ShipType.DESTROYER, Coordinate(0,4), "D"))
    }

    /**
     * Overloads the indexing operator
     */
    operator fun get(at: Coordinate): PositionStateBoard = getMove(at)
    /**
     * Gets the move at the given coordinates.
     * @param at the move's coordinates
     * @return the [PositionStateBoard] instance that made the move
     */
    fun getMove(at: Coordinate): PositionStateBoard = tiles[at.row][at.column]
    /**
     * Makes a move (A.K.A shoot) at the given coordinates and returns the new board instance.
     * @param at the board's coordinate
     * TODO: @throws IllegalArgumentException if the position is already occupied
     * @return the new board instance
     */
    fun makeMove(at: Coordinate): Board {
        return Board(
            turn = turn.other,
            tiles = tiles.mapIndexed { row, elem ->
                if (row == at.row)
                    MutableList(tiles[row].size) { col ->
                        if (col == at.column) PositionStateBoard(
                            boardPosition = Coordinate(row, col),
                            wasShoot = true,
                            wasShip = false,
                            shipType = null,
                            shipLayout = null
                        )
                        else tiles[row][col]
                    }
                else
                    elem
            }.toMutableList()
        )
    }

    /**
     * Converts this instance to a list of moves.
     */
    fun toMovesList(): MutableList<PositionStateBoard?> = tiles.flatten().toMutableList()
    fun toDocumentContent() = mapOf(
        TURN_FIELD to turn.name,
        BOARD_FIELD to toMovesList().joinToString(separator = "") {
            when {
                it?.wasShoot == true && it.wasShip == true -> "X"
                it?.wasShoot == true && it.wasShip == false -> "O"
                else -> "-"
            }
        },
        FLEET_FIELD to toShipsListString()
    )
    private fun toShipsListString(): String {
        // Convert each ship to its string representation
        val shipStrings = ships.map { ship ->
            "(${ship.shipType},${ship.coordinate},${ship.orientation})"
        }
        // Join the ship strings with commas and encapsulate them with square brackets
        return "[" + shipStrings.joinToString(",") + "]"
    }

    companion object {
        /**
         * Converts a list of moves to a board instance.
         * Converts a list of ships to a list of ships instance.
         * Puts the ships on the board.
         *
         * @param valueOf the next player to move
         * @param moves the moves list
         * @param fleet the fleet list
         * @return the new board instance
         */
        fun fromMovesList(valueOf: PlayerMarker, moves: String, fleet: String): Board {
            val board = Board(
                turn = valueOf,
                tiles = MutableList(
                    size = BOARD_SIDE,
                    init = { row ->
                        MutableList(size = BOARD_SIDE, init = { col ->
                            when (moves[row * BOARD_SIDE + col]) {
                                'X' -> PositionStateBoard(
                                    boardPosition = Coordinate(row, col),
                                    wasShoot = true,
                                    wasShip = true,
                                    shipType = null,
                                    shipLayout = null
                                )

                                'O' -> PositionStateBoard(
                                    boardPosition = Coordinate(row, col),
                                    wasShoot = true,
                                    wasShip = false,
                                    shipType = null,
                                    shipLayout = null
                                )

                                else -> PositionStateBoard(
                                    boardPosition = Coordinate(row, col),
                                    wasShoot = false,
                                    wasShip = false,
                                    shipType = null,
                                    shipLayout = null
                                )
                            }
                        })
                    })
            )
            val ships = parseShipListString(fleet)
            board.placeFleetOnBoard(ships)
            return board
        }
        /**
         * Parses a ship string into a [Ship] instance.
         * @param shipString the ship string
         * @return the [Ship] instance
         * TODO: @throws IllegalArgumentException if the ship string is invalid
         */
        private fun parseShipString(shipString: String): Ship? {
            // Remove any extra parentheses and whitespace
            val cleanedString = shipString.trim('(', ')')

            // Split the cleaned string into parts using ","
            val parts = cleanedString.split(",")

            // Ensure there are exactly three parts: shipType, coordinate, and orientation
            if (parts.size == 4) {
                try {
                    val shipType = ShipType.valueOf(parts[0].trim())
                    val coordinate1 = parts[1].removePrefix("(").toInt()
                    val coordinate2 = parts[2].removeSuffix(")").toInt()
                    val coordinate = Coordinate(coordinate1, coordinate2)
                    val orientation = parts[3].trim()

                    return Ship(shipType, coordinate, orientation)
                } catch (e: Exception) {
                    // Handle any parsing errors here
                    println("Error parsing ship string: $e")
                }
            }
            return null // Invalid shipString format
        }
        /**
         * Parses a list of ship strings into a list of [Ship] instances.
         * @param shipListString the list of ship strings
         * @return the list of [Ship] instances
         * TODO: @throws IllegalArgumentException if any of the ship strings are invalid
         */
        private fun parseShipListString(shipListString: String): MutableList<Ship> {
            // Remove square brackets and split the string into individual ship strings
            val shipStrings = shipListString.trim('[', ']').split("),(")

            // Create a list to store the parsed ships
            val ships = mutableListOf<Ship>()

            // Parse each ship string and add it to the list
            for (shipString in shipStrings) {
                val parsedShip = parseShipString(shipString)
                if (parsedShip != null) {
                    ships.add(parsedShip)
                }
            }
            return ships
        }
    }
    /**
     * Places the ships on the board.
     * @param ships the list of ships
     * TODO: @throws IllegalArgumentException if there is a ship collision
     */
    fun placeFleetOnBoard(ships: MutableList<Ship>) {
        for (ship in ships) {
            val shipType = ship.shipType
            val shipCoordinates = getShipCoordinates(ship)
            for ((row, col) in shipCoordinates) {
                val position = getMove(Coordinate(row, col))
                if (position.wasShip!=null) {
                    if (position.wasShip!!) {
                        //throw IllegalArgumentException("Ship collision at (${row},${col})")
                    }
                }
                position.wasShip = true
                position.shipType = shipType
                position.shipLayout = ship.orientation
            }
        }
    }
    /**
     * Gets the coordinates of a ship.
     * @param ship the ship
     * @return the list of coordinates with the ship's position and orientation applied
     * TODO: @throws IllegalArgumentException if the ship orientation is invalid
     */
    private fun getShipCoordinates(ship: Ship): List<Pair<Int, Int>> {
        val (startRow, startCol) = ship.coordinate
        val length = when (ship.orientation) {
            "R" -> ship.shipType.size
            "L" -> ship.shipType.size
            "U" -> ship.shipType.size
            "D" -> ship.shipType.size
            else -> throw IllegalArgumentException("Invalid orientation: ${ship.orientation}")
        }

        val coordinates = mutableListOf<Pair<Int, Int>>()

        when (ship.orientation) {
            "R" -> {
                for (i in 0 until length) {
                    coordinates.add(Pair(startRow, startCol + i))
                }
            }
            "L" -> {
                for (i in 0 until length) {
                    coordinates.add(Pair(startRow, startCol - i))
                }
            }
            "U" -> {
                for (i in 0 until length) {
                    coordinates.add(Pair(startRow - i, startCol))
                }
            }
            "D" -> {
                for (i in 0 until length) {
                    coordinates.add(Pair(startRow + i, startCol))
                }
            }
        }

        return coordinates
    }

}

// TODO: Implement the results of the game (win, lose, tie)

fun Board.hasWon(playerMarker: PlayerMarker): Boolean {
    // Check if all ships of have been sunk
    if (playerMarker != turn) {
        val flattenTiles = tiles.flatten()
        val filteredList = flattenTiles.filter {
            if (it.wasShip != null) {
                it.wasShip!! && it.wasShoot
            } else
                false
        }
        //Log.v(TAG_MODEL, "hasWon: ${filteredList.size} == ${ShipType.getShipsAllTiles()}")
        if (filteredList.size == 2/*ShipType.getShipsAllTiles()*/) { // TODO: Change this to the number of tiles of all ships
            return true
        }
        return false
    }
    return false
}

/**
 * Sum type used to describe board results occurring while the match is ongoing.
 * [HasWinner] to signal that the game have a winner.
 * [Tied] to signal that the game is tied.
 * [OnGoing] to signal the game is still ongoing.
 */
open class BoardResult(winner: PlayerMarker? = null) {
    companion object {
        fun getWinner(winner: BoardResult): PlayerMarker? {
            return when (winner) {
                is HasWinner -> winner.winner
                is Tied -> null
                else -> null
            }
        }
    }
}

class HasWinner(val winner: PlayerMarker) : BoardResult(winner)
class Tied : BoardResult(null)
class OnGoing : BoardResult(null)

/**
 * Gets the current result of the board.
 */
fun Board.getResult(): BoardResult = when {
        hasWon(PlayerMarker.PLAYER1) -> HasWinner(PlayerMarker.PLAYER1)
        hasWon(PlayerMarker.PLAYER2) -> HasWinner(PlayerMarker.PLAYER2)
        //isTied() -> Tied()
        else -> OnGoing()
    }