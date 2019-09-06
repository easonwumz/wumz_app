package com.wu.product.customView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wu.product.R;
import com.wu.product.animation.widget.TitleViewLayout;

import butterknife.BindView;

/**
 * @Description: description
 * @Author: wumz
 * @CreateDate: 2019/8/23 15:38
 */
public class CustomViewActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    TitleViewLayout titleView;

    public static Intent getLaunchIntent(Context context){
        return new Intent(context, CustomViewActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view);

        titleView.setTitle("自定义View");
        titleView.setTitleColor(Color.BLUE);
        titleView.setTitleSize(23);
    }
}
