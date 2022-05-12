package hu.ait.connectussy

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class BoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var paintBackground: Paint = Paint()
    private lateinit var paintLine: Paint
    private lateinit var paintCross: Paint
    private lateinit var paintCircle: Paint
    private lateinit var paintText: Paint


    private var bitmapBg: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.monet)

    init {
        paintBackground.color = Color.BLACK
        paintBackground.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintCross = Paint()
        paintCross.color = Color.RED
        paintCross.style = Paint.Style.STROKE
        paintCross.strokeWidth = 5f

        paintCircle = Paint()
        paintCircle.color = Color.GREEN
        paintCircle.style = Paint.Style.STROKE
        paintCircle.strokeWidth = 5f

        paintText = Paint()
        paintText.color = Color.GREEN
        paintText.textSize = 70f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintText.textSize = h / 3f

        bitmapBg = Bitmap.createScaledBitmap(
            bitmapBg,
            width, height, false
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(
            0f, 0f, width.toFloat(),
            height.toFloat(), paintBackground
        )

        canvas?.drawBitmap(bitmapBg, 0f, 0f, null)

        drawGameArea(canvas!!)

        drawPlayers(canvas)
    }

    private fun drawGameArea(canvas: Canvas) {
        // border
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        // six horizontal lines
        canvas.drawLine(
            0f, (height / 7).toFloat(), width.toFloat(), (height / 7).toFloat(),
            paintLine
        )
        canvas.drawLine(
            0f, (2 * height / 7).toFloat(), width.toFloat(),
            (2 * height / 7).toFloat(), paintLine
        )
        canvas.drawLine(
            0f, (3 * height / 7).toFloat(), width.toFloat(),
            (3 * height / 7).toFloat(), paintLine
        )
        canvas.drawLine(
            0f, (4 * height / 7).toFloat(), width.toFloat(),
            (4 * height / 7).toFloat(), paintLine
        )
        canvas.drawLine(
            0f, (5 * height / 7).toFloat(), width.toFloat(),
            (5 * height / 7).toFloat(), paintLine
        )
        canvas.drawLine(
            0f, (6 * height / 7).toFloat(), width.toFloat(),
            (6 * height / 7).toFloat(), paintLine
        )

        // six vertical lines
        canvas.drawLine(
            (width / 7).toFloat(), 0f, (width / 7).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (2 * width / 7).toFloat(), 0f, (2 * width / 7).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (3 * width / 7).toFloat(), 0f, (3 * width / 7).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (4 * width / 7).toFloat(), 0f, (4 * width / 7).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (5 * width / 7).toFloat(), 0f, (5 * width / 7).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (6 * width / 7).toFloat(), 0f, (6 * width / 7).toFloat(), height.toFloat(),
            paintLine
        )
    }

    private fun drawPlayers(canvas: Canvas) {
        for (i in 0..6) {
            for (j in 0..6) {
                if (BoardModel.getFieldContent(i, j) == BoardModel.CIRCLE ||
                        BoardModel.getFieldContent(i,j) == BoardModel.CROSS) {
                    val centerX = (i * width / 7 + width / 14).toFloat()
                    val centerY = (j * height / 7 + height / 14).toFloat()
                    val radius = height / 14 - 6

                    canvas.drawCircle(centerX, centerY, radius.toFloat(), paintCircle)
                }

            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width / 7)
            val tY = event.y.toInt() / (height / 7)

            if (tX < 7 && tY < 7 && BoardModel.getFieldContent(tX, tY) ==
                BoardModel.EMPTY
            ) {
                BoardModel.setFieldContent(tX, tY, BoardModel.getNextPlayer())
                invalidate()
            }

            if (BoardModel.getNextPlayer() == BoardModel.CIRCLE) {
                (context as MainActivity).showText("Player O's turn")
            }
            if (BoardModel.getNextPlayer() == BoardModel.CROSS) {
                (context as MainActivity).showText("Player X's turn")
            }

            if (BoardModel.getWinner() == BoardModel.CIRCLE) {
                (context as MainActivity).showText("Winner is Player O!")
            }
            if (BoardModel.getWinner() == BoardModel.CROSS) {
                (context as MainActivity).showText("Winner is Player X!")
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

    fun resetGame() {
        BoardModel.resetModel()
        invalidate()
    }

    fun changePlayer() {
        BoardModel.changeNextPlayer()
    }
}