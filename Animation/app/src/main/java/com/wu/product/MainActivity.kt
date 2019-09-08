package com.wu.product

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.wu.product.animation.ui.AnimationActivity
import com.wu.product.base.BaseActivity
import com.wu.product.customView.ShapeDrawActivity
import com.wu.product.map.MapActivity
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @Description: description
 * @Author: wumz
 * @CreateDate: 2019/8/30 10:39
 */
class MainActivity : BaseActivity(), MultiItemTypeAdapter.OnItemClickListener {

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        val list = ArrayList<String>()
        list.add("动画")
        list.add("自定义view")
        list.add("地图")

        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView?.setHasFixedSize(true)
        val adapter = object : CommonAdapter<String>(this, android.R.layout.simple_list_item_1, list) {
            override fun convert(holder: ViewHolder?, s: String?, position: Int) {
                holder?.setText(android.R.id.text1, s)
            }
        }
        recyclerView?.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
        when (position) {
            0 -> startActivity(AnimationActivity.getLaunchIntent(this))
            1 -> startActivity(ShapeDrawActivity.getLaunchIntent(this))
            2 -> startActivity(MapActivity.getLaunchIntent(this))
        }
    }

    override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
        return false
    }
}
