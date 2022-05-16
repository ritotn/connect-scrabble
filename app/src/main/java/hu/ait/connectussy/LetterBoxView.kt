package hu.ait.connectussy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import java.lang.reflect.Type

class LetterBoxView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paintBackground: Paint = Paint()
    private var paintLine: Paint
    private var paintLetter: Paint
    private var paintHighlighted: Paint


    private val numLetters = 5
    private var letterSet = BoardModel.getRandomLetters(numLetters).toMutableList()

    init {
        paintBackground.color = Color.WHITE
        paintBackground.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.LTGRAY
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 10f

        paintLetter = Paint()
        paintLetter.color = ContextCompat.getColor(context!!, R.color.gray)
        paintLetter.typeface = Typeface.DEFAULT_BOLD

        paintHighlighted = Paint()
        paintHighlighted.style = Paint.Style.FILL
        paintHighlighted.color = ContextCompat.getColor(context!!, R.color.yellow)
        paintHighlighted.typeface = Typeface.DEFAULT_BOLD
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintLetter.textSize = w / 7.5f
        paintHighlighted.textSize = w / 7.5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLetterBox(canvas)
        drawLetters(canvas)
    }

    private fun drawLetterBox(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        for (i in 1..6) {

            canvas.drawLine(
                (i * width / 5).toFloat(), 0f, (i * width / 5).toFloat(), height.toFloat(),
                paintLine
            )

        }
    }

    private fun drawLetters(canvas: Canvas) {
        for (letter in letterSet) {
            var widthOffset = width / 45f
            val heightOffset = height / 3.75f
            when (letter) {
                "I" -> { // shift "I" to the right a bit
                    widthOffset = width/45f - width/135f
                }
                "M" -> { // shift "M" to the left a bit
                    widthOffset = width/45f + width/135f
                }
                "W" -> { // shift "M" to the left a bit
                    widthOffset = width/45f + width/135f
                }
            }
            var paint = paintLetter
            if (letterSet.indexOf(letter) == BoardModel.getBoxCellHighlighted()) {
                paint = paintHighlighted
            }
            canvas.drawText(
                letter,
                ((letterSet.indexOf(letter)) * width / 5 + width / widthOffset).toFloat(),
                (height - heightOffset).toFloat(),
                paint
            )
        }
    }

    // what happens when a player touches the Letter Box
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {

            // get coordinate of where they touched
            val tX = (event.x.toInt() / (width / 5))

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