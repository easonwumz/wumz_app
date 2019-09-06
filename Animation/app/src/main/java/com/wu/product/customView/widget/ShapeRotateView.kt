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
class ShapeRotateView : View {

    private lateinit var mPaint: Paint
    private lateinit var rectF: RectF

    private var mWidth: Int = 0
    private var mHeight: Int = 0

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
        mPaint.strokeWidth = 10f
        mPaint.style = Paint.Style.FILL

        rectF = RectF(-400f, -400f, 400f, 400f)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate((mWidth / 2).toFloat(), (mHeight / 2).toFloat())

        mPaint.color = Color.YELLOW
        canvas?.drawCircle(0f, 0f, 150f, mPaint);
//        canvas?.drawCircle(0f, 0f, 100f, mPaint)

        mPaint.style = Paint.Style.STROKE
        for (i in 0..360) {
            canvas?.drawLine(0f, 200f, 0f, 300f, mPaint)
            canvas?.rotate(30f)
        }
    }
}