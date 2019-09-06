package com.wu.product.animation.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.wu.product.R;

/**
 * @Description: description
 * @Author: wumz
 * @CreateDate: 2019/8/23 15:31
 */
public class TitleViewLayout extends ConstraintLayout {

    private TextView titleText;


    public TitleViewLayout(Context context) {
        this(context, null);
    }

    public TitleViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_topview, this, true);
        titleText = findViewById(R.id.layout_top_title);
        titleText.setTextColor(Color.RED);
        titleText.setTextSize(12);

        TypedArray ad = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        ad.recycle();
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            titleText.setText(title);
        }
    }

    public void setTitleColor(int color) {
        titleText.setTextColor(color);
    }

    public void setTitleSize(int size) {
        titleText.setTextSize(size);
    }
}
