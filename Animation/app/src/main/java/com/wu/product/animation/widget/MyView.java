package com.wu.product.animation.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wu.product.R;

/**
 * @Description: description
 * @Author: wumz
 * @CreateDate: 2019/8/23 14:17
 */
public class MyView extends View {

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        String text = ta.getString(R.styleable.MyView_text);
        int textSize = ta.getInteger(R.styleable.MyView_textSize, 0);
        boolean isWu = ta.getBoolean(R.styleable.MyView_isWu, false);
        Log.e("wu", "text:" + text + ", textSize:" + textSize + ", isWu:" + isWu);
        ta.recycle();
    }
}
