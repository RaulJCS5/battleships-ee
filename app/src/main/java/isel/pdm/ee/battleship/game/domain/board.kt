package isel.pdm.ee.battleship.game.domain


const val BOARD_FIELD= "board"
const val TURN_FIELD = "turn"
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
    init {
        ships.add(Ship(ShipType.CARRIER, Coordinate(0,0), "D"))
        ships.add(Ship(ShipType.BATTLESHIP, Coordinate(0,1), "D"))
        ships.add(Ship(ShipType.CRUISER, Coordinate(0,2), "D"))
        ships.add(Ship(ShipType.SUBMARINE, Coordinate(0,3), "D"))
        ships.add(Ship(ShipType.DESTROYER, Coordinate(0,4), "D"))
    }

    operator fun get(at: Coordinate): PositionStateBoard = getMove(at)

    fun getMove(at: Coordinate): PositionStateBoard = tiles[at.row][at.column]

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

        fun parseShipString(shipString: String): Ship? {
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

        fun parseShipListString(shipListString: String): MutableList<Ship> {
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

fun isTied(): Boolean = false

fun hasWon(playerMarker: PlayerMarker): Boolean = false


open class BoardResult
class HasWinner(val winner: PlayerMarker) : BoardResult()
class Tied : BoardResult()
class OnGoing : BoardResult()

fun Board.getResult(): BoardResult =
    when {
        hasWon(PlayerMarker.PLAYER1) -> HasWinner(PlayerMarker.PLAYER1)
        hasWon(PlayerMarker.PLAYER2) -> HasWinner(PlayerMarker.PLAYER2)
        toMovesList().all { it != null } -> Tied()
        else -> OnGoing()
    }