package hu.ait.connectussy

import android.content.Context
import android.graphics.*
import android.provider.Settings.Global.getString
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat.getColor
import com.afollestad.materialdialogs.MaterialDialog

class BoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var paintBackground: Paint = Paint()
    private var paintLine: Paint
    private var paintLetter: Paint
    private var paintHighlighted: Paint
    private var paintHighlightedCell: Paint
    private var paintNormalCell: Paint
    //private var paintHCellBorder: Paint
    private var bitmapBg: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.monet)

    init {
        paintBackground.color = Color.LTGRAY
        paintBackground.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = ContextCompat.getColor(context!!, R.color.white)
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintLetter = Paint()
        paintLetter.typeface = Typeface.DEFAULT_BOLD
        paintLetter.color = Color.WHITE

        paintHighlightedCell = Paint()
        paintHighlightedCell.color = ContextCompat.getColor(context!!, R.color.yellow)
        paintHighlightedCell.style = Paint.Style.FILL

        paintNormalCell = Paint()
        paintNormalCell.color = ContextCompat.getColor(context!!, R.color.gray)

        paintHighlighted = Paint()
        paintHighlighted.color = Color.BLACK

        BoardModel.setBoardCellLetter(3, 3, BoardModel.getRandomLetter())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintLetter.textSize = h / 7f
        paintHighlighted.textSize = h / 7f

        bitmapBg = Bitmap.createScaledBitmap(
            bitmapBg,
            width, height, false
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBoard(canvas!!)
        drawLetters(canvas!!)
    }

    private fun drawBoard(canvas: Canvas) {
        // background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)
        // border
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        // six horizontal and vertical lines
        for (i in 1..6) {
            canvas.drawLine(
                0f, (i * height / 7).toFloat(), width.toFloat(), (i * height / 7).toFloat(),
                paintLine
            )
            canvas.drawLine(
                (i * width / 7).toFloat(), 0f, (i * width / 7).toFloat(), height.toFloat(),
                paintLine
            )
        }
    }

    private fun drawLetters(canvas: Canvas) {
        for (i in 0..6) {
            for (j in 0..6) {
                val letter = BoardModel.getBoardCellLetter(i, j)
                if (letter != "") {
                    var widthOffset = 42
                    val heightOffset = 42
                    when (letter) {
                        "I" -> { // shift "I" to the right a bit
                            widthOffset = 21
                        }
                        "M" -> { // shift "M" to the left a bit
                            widthOffset = 84
                        }
                        "W" -> { // shift "W" to the left a bit
                            widthOffset = 84
                        }
                    }
                    var paintCell = paintNormalCell
                    var paint = paintLetter
                    if (Pair(i, j) == BoardModel.getBoardCellHighlighted()) {
                        paintCell = paintHighlightedCell
                        canvas.drawRect(
                            (width/7*i).toFloat(),
                            (height/7*j).toFloat(),
                            ((width/7) + (width/7*i)).toFloat(),
                            ((height/7) + (height/7*j)).toFloat(),
                            paintCell
                        )
                        canvas.drawRect(
                            (2.5 + width/7*i).toFloat(),
                            (2.5 + height/7*j).toFloat(),
                            (2.5 + (width/7) + (width/7*i)).toFloat(),
                            (2.5 + (height/7) + (height/7*j)).toFloat(),
                            paintLine
                        )
                    } else {
                        paintCell = paintNormalCell
                        canvas.drawRect(
                            (width/7*i).toFloat(),
                            (height/7*j).toFloat(),
                            ((width/7) + (width/7*i)).toFloat(),
                            ((height/7) + (height/7*j)).toFloat(),
                            paintCell
                        )
                        canvas.drawRect(
                            (2.5 + width/7*i).toFloat(),
                            (2.5 + height/7*j).toFloat(),
                            (2.5 + (width/7) + (width/7*i)).toFloat(),
                            (2.5 + (height/7) + (height/7*j)).toFloat(),
                            paintLine
                        )
                    }
                    canvas.drawText(
                        letter,
                        (i * width / 7 + width / widthOffset).toFloat(),
                        ((j + 1) * height / 7 - height / heightOffset).toFloat(),
                        paint
                    )
                }

            }
        }
    }

    // what happens when a player touches the board
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {

            // get coordinate of where they touched
            val tX = event.x.toInt() / (width / 7)
            val tY = event.y.toInt() / (height / 7)

            val selectedLetter = BoardModel.getSelectedLetter()

            // if they haven't already placed another letter this turn,
            // and the cell they touched is a placeable cell,
            // place letter there
            if (BoardModel.getBoardCellHighlighted() == Pair(-1, -1) &&
                BoardModel.isPlaceable(tX, tY)
            ) {
                BoardModel.setBoardCellLetter(tX, tY, selectedLetter)
                BoardModel.setBoardCellHighlighted(tX, tY)
                invalidate()
            }

            // if cell they touched has last placed letter, remove it
            else if ((Pair(tX, tY) == BoardModel.getBoardCellHighlighted())) {
                BoardModel.setBoardCellLetter(tX, tY, "")
                BoardModel.setBoardCellHighlighted(-1, -1)
                invalidate()
            }
        }

        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }

    // what happens when a player presses "Restart Game" button
    fun resetGame() {
        BoardModel.resetModel()
        (context as MainActivity).showText("${BoardModel.getCurrentPlayer()}, it's your turn!")
        invalidate()
    }

    // what happens when a player presses "Play Turn" button
    fun playTurn() {
        var text = ""
        // check is letter is placed
        if (BoardModel.getBoardCellHighlighted() == Pair(-1, -1)) {
            text = "${BoardModel.getCurrentPlayer()}: Please place a letter!"
        } else {
            BoardModel.setBoardCellHighlighted(-1, -1)
            // check new game status
            when (BoardModel.getGameStatus()) {
                BoardModel.GAMENOTOVER -> {
                    BoardModel.changeCurrentPlayer()
                    text = "${BoardModel.getCurrentPlayer()}, it's your turn!"
                }
                BoardModel.GAMEOVERTIE -> {
                    text = "It's a tie!"
                    MaterialDialog(context).show {
                        title(R.string.game_over)
                        message(R.string.tie_game)
                        positiveButton(R.string.play_again) { dialog ->
                            BoardModel.resetModel()
                            resetGame()
                        }
                    }
                }
                BoardModel.GAMEOVERWIN -> {
                    text = "${BoardModel.getCurrentPlayer()} wins! \uD83C\uDF89"
                    MaterialDialog(context).show {
                        title(R.string.game_over)
                        message(text = "${BoardModel.getCurrentPlayer()} wins! The winning word was " +
                                "${BoardModel.getWinningWord()}.")
                        positiveButton(R.string.play_again) { dialog ->
                            BoardModel.resetModel()
                            resetGame()
                        }
                    }
                }
            }

        }

        invalidate()
        (context as MainActivity).showText(text)
    }
}