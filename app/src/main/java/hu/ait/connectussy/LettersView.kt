package hu.ait.connectussy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class LettersView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paintBackground: Paint = Paint()
    private var paintLine: Paint
    private var paintLetter: Paint
    private var paintHighlighted: Paint

    init {
        paintBackground.color = Color.HSVToColor(floatArrayOf(3.5f, .25f, 1f))
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
        // TODO: Draw 5 letters that players can choose from
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        /* TODO: If letter is not already placed on the board,
                 player can select letter from LetterBox
                 Then, BoardView.setSelectedLetter(letter) is called
                 (I might be missing some other functionalities too)
         */
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(w, h)
    }
}