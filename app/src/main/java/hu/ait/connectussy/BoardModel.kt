package hu.ait.connectussy

import com.afollestad.materialdialogs.MaterialDialog
import hu.ait.connectussy.databinding.ActivityMainBinding

object BoardModel {
    const val PLAYER1 = "Player 1"
    const val PLAYER2 = "Player 2"

    const val GAMENOTOVER = "Game Not Over"
    const val GAMEOVERWIN = "Game Over Win"
    const val GAMEOVERTIE = "Game Over Tie"

    private val model = arrayOf(
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", ""),
    )

    // Ranges calculated from data found at
    // http://en.wikipedia.org/wiki/Letter_frequency
    private val letterFrequency = mapOf(
        "A" to 8167, "B" to 9659, "C" to 12441, "D" to 16694, "E" to 29396,
        "F" to 31624, "G" to 33639, "H" to 39733, "I" to 46699, "J" to 46852,
        "K" to 47624, "L" to 51649, "M" to 54055, "N" to 60804, "O" to 68311,
        "P" to 70240, "Q" to 70335, "R" to 76322, "S" to 82649, "T" to 91705,
        "U" to 94463, "V" to 95441, "W" to 97801, "X" to 97951, "Y" to 99925,
        "Z" to 100000
    )

    private var highlightedBoardCell = Pair(-1, -1)
    private var highlightedBoxCell = -1
    private var selectedLetter = ""
    private var winningWord = "" // TODO: Use this variable in GameOver Dialog

    private var currentPlayer = PLAYER1

    fun getBoardCellLetter(x: Int, y: Int) = model[x][y]

    fun setBoardCellLetter(x: Int, y: Int, letter: String) {
        model[x][y] = letter
    }

    fun getWinningWord() = winningWord

    fun getBoardCellHighlighted() = highlightedBoardCell

    fun setBoardCellHighlighted(x: Int, y: Int) {
        highlightedBoardCell = Pair(x, y)
    }

    fun getBoxCellHighlighted() = highlightedBoxCell

    fun setBoxCellHighlighted(x: Int) {
        highlightedBoxCell = x
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

    fun getRandomLetter(): String {
        val random = Math.random() * 100000

        for (letter in letterFrequency) {
            if (letter.value > random) {
                return letter.key
            }
        }

        return ""
    }

    // will always be a distinct set (no duplicate letters)
    fun getRandomLetters(quantity: Int): MutableSet<String> {
        val letters = mutableSetOf<String>()

        while (letters.size < quantity) {
            letters.add(getRandomLetter())
        }

        return letters
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
        winningWord = isWordFormed()
        if (winningWord != "") {
            return GAMEOVERWIN
        }

        return GAMENOTOVER
    }

    // returns winning word if present, empty otherwise
    private fun isWordFormed(): String {
        // create all possible words
        val possibleWords = mutableListOf<String>()

        // 28 * 2 possible horizontal words ➡️
        for (i in 0..6) {
            for (j in 0..3) {
                var possibleWord = ""
                for (k in 0..3) {
                    possibleWord += model[i][j + k]
                }
                possibleWords.add(possibleWord)
                possibleWords.add(possibleWord.reversed())
            }

        }
        // 28 * 2 possible vertical words ⬇️
        for (j in 0..6) {
            for (i in 0..3) {
                var possibleWord = ""
                for (k in 0..3) {
                    possibleWord += model[i + k][j]
                }
                possibleWords.add(possibleWord)
                possibleWords.add(possibleWord.reversed())
            }
        }
        // 16 * 2 possible diagonal (top-left to bottom-right) words ↘️
        for (i in 0..3) {
            for (j in 0..3) {
                var possibleWord = ""
                for (k in 0..3) {
                    possibleWord += model[i + k][j + k]
                }
                possibleWords.add(possibleWord)
                possibleWords.add(possibleWord.reversed())
            }
        }
        // 16 * 2 possible diagonal (bottom-left to top-right) words ↗️
        for (i in 3..6) {
            for (j in 0..3) {
                var possibleWord = ""
                for (k in 0..3) {
                    possibleWord += model[i - k][j + k]
                }
                possibleWords.add(possibleWord)
                possibleWords.add(possibleWord.reversed())
            }
        }

        // check if any of the possible words are valid
        for (possibleWord in possibleWords) {
            if (possibleWord in ValidWords.getValidWords()) {
                return possibleWord
            }
        }
        return ""
    }

    fun resetModel() {
        for (i in 0..6) {
            for (j in 0..6) {
                model[i][j] = ""
            }
        }
        setBoardCellLetter(3, 3, getRandomLetter())
        currentPlayer = PLAYER1
    }
}