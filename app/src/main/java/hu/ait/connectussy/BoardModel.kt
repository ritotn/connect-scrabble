package hu.ait.connectussy

object BoardModel {
    const val PLAYER1 = "Player 1"
    const val PLAYER2 = "Player 2"

    const val GAMENOTOVER = "Game Not Over"
    const val GAMEOVERWIN = "Game Over Win"
    const val GAMEOVERTIE = "Game Over Tie"

    public val LETTERS = arrayOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        ""
    )

    private val model = arrayOf(
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
    )

    private var highlightedCell = Pair(-1,-1)
    private var selectedLetter = "A"

    private var currentPlayer = PLAYER1

    fun getCellLetter(x: Int, y: Int) = model[x][y]

    fun setCellLetter(x: Int, y: Int, letter: String) {
        model[x][y] = letter
    }

    fun getCellHighlighted() = highlightedCell

    fun setCellHighlighted(x: Int, y: Int) {
        highlightedCell = Pair(x, y)
    }

    fun getCurrentPlayer() = currentPlayer

    fun changeCurrentPlayer() {
        currentPlayer = if (currentPlayer == PLAYER1) PLAYER2 else PLAYER1
    }

    fun getSelectedLetter(): String {
        return selectedLetter
    }

    fun setSelectedLetter(letter: String) {
        selectedLetter = letter
    }

    // check if a cell is placeable
    // - if the cell is currently unfilled AND
    // - any adjacent cells are already filled
    fun isPlaceable(x: Int, y: Int): Boolean {
        // check if cell is currently filled
        if (model[x][y] != "") return false

        // get all surrounding cells (including out of bounds)
        val adjacentCells = mutableListOf(
            Pair(x - 1, y - 1),
            Pair(x - 1, y),
            Pair(x - 1, y + 1),
            Pair(x, y - 1),
            Pair(x, y + 1),
            Pair(x + 1, y - 1),
            Pair(x + 1, y),
            Pair(x + 1, y + 1)
        )
        for (pair in adjacentCells) {
            val (x_coord, y_coord) = pair
            // ignore out of bound cells
            if (x_coord < 0 || x_coord > 6 || y_coord < 0 || y_coord > 6) {
                continue
            }
            // if in bound adjacent cell is filled, cell is placeable
            if (model[x_coord][y_coord] != "") {
                return true
            }
        }

        return false
    }

    fun getGameStatus(): String {
        // check if tie
        var filledCellCount = 0
        for (i in 0..6) {
            for (j in 0..6) {
                if (model[i][j] != "") filledCellCount += 1
            }
        }
        if (filledCellCount == 49) return GAMEOVERTIE

        // check if win
        // TODO: Check if any words are formed

        return GAMENOTOVER
    }

    fun resetModel() {
        for (i in 0..6) {
            for (j in 0..6) {
                model[i][j] = ""
            }
        }
        currentPlayer = PLAYER1
    }
}