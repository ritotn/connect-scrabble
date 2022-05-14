package hu.ait.connectussy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class LetterBoxView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paintBackground: Paint = Paint()
    private var paintLine: Paint
    private var paintLetter: Paint
    private var paintHighlighted: Paint

    private val numLetters = 5
    private var letterSet = BoardModel.getRandomLetters(numLetters).toMutableList()

    init {
        paintBackground.color = Color.HSVToColor(floatArrayOf(3.5f, .25f, 1f))
        paintBackground.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintLetter = Paint()
        paintLetter.color = Color.BLACK

        paintHighlighted = Paint()
        paintHighlighted.color = Color.RED

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintLetter.textSize = h / 7f
        paintHighlighted.textSize = h / 7f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLetterBox(canvas)
        drawLetters(canvas)
    }

    private fun drawLetterBox(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)
        // TODO: Make this look nicer
    }

    private fun drawLetters(canvas: Canvas) {
        for (letter in letterSet) {
            var widthOffset = 42
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
            if (letterSet.indexOf(letter) == BoardModel.getBoxCellHighlighted()) {
                paint = paintHighlighted
            }
            canvas.drawText(
                letter,
                ((letterSet.indexOf(letter) + 1) * width / 7 + width / widthOffset).toFloat(),
                (height / 2).toFloat(),
                paint
            )
        }
    }

    // what happens when a player touches the Letter Box
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {

            // get coordinate of where they touched
            val tX = (event.x.toInt() / (width / 7)) - 1

            // if they touched one of the letters
            if (tX in 0..4) {
                val selectedLetter = letterSet.elementAt(tX)
                // if they haven't already placed a letter this turn,
                // select this letter
                if (BoardModel.getBoardCellHighlighted() == Pair(-1, -1)) {
                    BoardModel.setSelectedLetter(selectedLetter)
                    BoardModel.setBoxCellHighlighted(tX)
                    invalidate()
                }
            }

        }

        return true
    }

    // what happens when a player presses "Play Turn" button
    fun playTurn() {
        if (BoardModel.getBoardCellHighlighted() != Pair(-1, -1)) {
            // replace placed letter with a new one that's distinct
            var newLetter = BoardModel.getRandomLetter()
            while (newLetter in letterSet) {
                newLetter = BoardModel.getRandomLetter()
            }
            letterSet[BoardModel.getBoxCellHighlighted()] = newLetter
            BoardModel.setBoxCellHighlighted(-1)
        }
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(w, h)
    }
}