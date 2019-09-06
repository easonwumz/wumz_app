package com.wu.product.animation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.wu.product.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnimationActivity extends AppCompatActivity implements MultiItemTypeAdapter.OnItemClickListener {

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, AnimationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);

        List<String> list = new ArrayList<>();
        list.add("平移动画");
        list.add("缩放动画");
        list.add("旋转动画");
        list.add("透明度动画");
        list.add("属性动画");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        CommonAdapter adapter = new CommonAdapter<String>(this, android.R.layout.simple_list_item_1, list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(android.R.id.text1, s);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    Animation animation;

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                animation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.view_anim_translate);
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.view_anim_scale);
                break;
            case 2:
                animation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.view_anim_rotate);
                break;
            case 3:
                animation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.view_anim_alpha);
                break;
            case 4:
                startActivity(new Intent(this, PropertyAnimationActivity.class));
                break;
        }
        if (animation != null) {
            imageView.startAnimation(animation);
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
