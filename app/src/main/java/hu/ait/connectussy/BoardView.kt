package hu.ait.connectussy

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class BoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var paintBackground: Paint = Paint()
    private var paintLine: Paint
    private var paintLetter: Paint
    private var paintHighlighted: Paint
    private var bitmapBg: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.monet)

    init {
        paintBackground.color = Color.BLACK
        paintBackground.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintLetter = Paint()
        paintLetter.color = Color.WHITE

        paintHighlighted = Paint()
        paintHighlighted.color = Color.RED

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

        // TODO: Make starting letter different each time based on frequency
        BoardModel.setCellLetter(3, 3, "E")

        drawBoard(canvas)
        drawLetters(canvas)
    }

    private fun drawBoard(canvas: Canvas) {
        // background
        canvas.drawBitmap(bitmapBg, 0f, 0f, null)
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
                val letter = BoardModel.getCellLetter(i, j)
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
                    var paint = paintLetter
                    if (Pair(i, j) == BoardModel.getCellHighlighted()) {
                        paint = paintHighlighted
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

            // if they haven't already placed another cell this turn,
            // and the cell they touched is a placeable cell,
            // place letter there
            if (BoardModel.getCellHighlighted() == Pair(-1, -1) &&
                BoardModel.isPlaceable(tX, tY)
            ) {
                BoardModel.setCellLetter(tX, tY, selectedLetter)
                BoardModel.setCellHighlighted(tX, tY)
                invalidate()
            }

            // if cell they touched is last placed cell, remove letter there
            else if ((Pair(tX, tY) == BoardModel.getCellHighlighted())) {
                BoardModel.setCellLetter(tX, tY, "")
                BoardModel.setCellHighlighted(-1, -1)
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
        (context as MainActivity).showText("${BoardModel.getCurrentPlayer()}'s turn")
        invalidate()
    }

    // what happens when a player presses "Play Turn" button
    fun playTurn() {
        var text = ""
        // check is letter is placed
        if (BoardModel.getCellHighlighted() == Pair(-1, -1)) {
            text = "${BoardModel.getCurrentPlayer()}: Please place a letter!"
        } else {
            BoardModel.setCellHighlighted(-1, -1)
            // check new game status
            when (BoardModel.getGameStatus()) {
                BoardModel.GAMENOTOVER -> {
                    BoardModel.changeCurrentPlayer()
                    text = "${BoardModel.getCurrentPlayer()}'s turn"
                }
                BoardModel.GAMEOVERTIE -> {
                    text = "It's a tie!"
                }
                BoardModel.GAMEOVERWIN -> {
                    text = "${BoardModel.getCurrentPlayer()} wins! \uD83C\uDF89"
                }
            }

        }

        invalidate()
        (context as MainActivity).showText(text)
    }
}