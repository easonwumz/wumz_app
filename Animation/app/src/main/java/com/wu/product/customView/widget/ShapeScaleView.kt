package com.wu.product.customView.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * @Description:     description
 * @Author:         wumz
 * @CreateDate:     2019/9/2 15:09
 */
class ShapeScaleView : View {

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
        mPaint.style = Paint.Style.STROKE

        rectF = RectF(-400f, -400f, 400f, 400f)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate((3 * mWidth / 4).toFloat(), 0f)
        for (i in 0..10) {
            canvas?.scale(0.8f, 0.8f)
            canvas?.drawRect(rectF, mPaint)
        }
    }
}