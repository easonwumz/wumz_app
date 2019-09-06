package com.wu.product.customView.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * @Description:     description
 * @Author:         wumz
 * @CreateDate:     2019/9/2 15:09
 */
class ShapeView : View {

    private lateinit var mPaint: Paint
    private lateinit var rectF: RectF

    private val value1 = 365f
    private val value2 = 30f
    private val value3 = 128f
    private val value4 = 58f
    private val value5 = 256f

    private var total: Float = 0f

    private var startAngle1 = -90f
    private var startAngle2: Float = 0.0f
    private var startAngle3: Float = 0.0f
    private var startAngle4: Float = 0.0f
    private var startAngle5: Float = 0.0f

    private var sweepAngle1: Float = 0.0f
    private var sweepAngle2: Float = 0.0f
    private var sweepAngle3: Float = 0.0f
    private var sweepAngle4: Float = 0.0f
    private var sweepAngle5: Float = 0.0f

    private var translateX: Float = 100f
    private var translateY: Float = 100f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun init() {
        mPaint = Paint();
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = 2.0f
        mPaint.style = Paint.Style.FILL

        rectF = RectF(0f, 0f, 400f, 400f)

        total = value1 + value2 + value3 + value4 + value5

        sweepAngle1 = (value1 / total) * 360

        startAngle2 = startAngle1 + sweepAngle1
        sweepAngle2 = (value2 / total) * 360

        startAngle3 = startAngle2 + sweepAngle2
        sweepAngle3 = (value3 / total) * 360

        startAngle4 = startAngle3 + sweepAngle3
        sweepAngle4 = (value4 / total) * 360

        startAngle5 = startAngle4 + sweepAngle4
        sweepAngle5 = (value5 / total) * 360
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate(translateX, translateY)

        mPaint.color = Color.RED
        drawArc(canvas, rectF, startAngle1, sweepAngle1, mPaint)
        mPaint.color = Color.GRAY
        drawArc(canvas, rectF, startAngle2, sweepAngle2, mPaint)
        mPaint.color = Color.GREEN
        drawArc(canvas, rectF, startAngle3, sweepAngle3, mPaint)
        mPaint.color = Color.BLUE
        drawArc(canvas, rectF, startAngle4, sweepAngle4, mPaint)
        mPaint.color = Color.YELLOW
        drawArc(canvas, rectF, startAngle5, sweepAngle5, mPaint)
    }

    private fun drawArc(canvas: Canvas?, rectF: RectF, startAngle: Float, sweepAngle: Float, paint: Paint) {
        canvas?.drawArc(rectF, startAngle, sweepAngle, true, paint)
    }

    fun setStartAngle(startAngle: Float) {
        this.startAngle1 = startAngle
        invalidate()
    }

    fun translate(dx: Float, dy: Float) {
        this.translateX = dx
        this.translateY = dy
        invalidate()
    }
}