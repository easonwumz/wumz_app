package com.wu.product.animation.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wu.product.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PropertyAnimationActivity extends Activity implements View.OnClickListener, MultiItemTypeAdapter.OnItemClickListener {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        imageView = findViewById(R.id.image);
        imageView.setOnClickListener(this);

        List<String> list = new ArrayList<>();
        list.add("平移");
        list.add("缩放");
        list.add("旋转");
        list.add("透明");

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
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

    @Override
    public void onClick(View v) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,
                "translationY", 0, 500F);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView,
                "scaleX", 1F, 2F);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView,
                "scaleY", 1F, 2F);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView,
                "alpha", 1F, 0.3F);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(imageView,
                "rotationX", 0, 360F);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(animator, animator1, animator2, animator3, animator4);
        set.start();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,
                        "translationY", 0, 500F);

                animator.setDuration(1000);
                animator.start();
                break;
            case 1:
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView,
                        "rotationX", 0, 360F);

                animator1.setDuration(1000);
                animator1.start();
                break;
            case 2:
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView,
                        "scaleY", 1F, 2F);

                animator2.setDuration(1000);
                animator2.start();
                break;
            case 3:
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView,
                        "scaleX", 1F, 2F);

                animator3.setDuration(1000);
                animator3.start();
                break;
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
