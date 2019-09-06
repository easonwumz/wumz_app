package com.wu.product.customView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.wu.product.R
import com.wu.product.base.BaseActivity
import kotlinx.android.synthetic.main.activity_shape.*

/**
 * @Description:     description
 * @Author:         wumz
 * @CreateDate:     2019/9/2 15:02
 */
class ShapeDrawActivity : BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent = Intent(context, ShapeDrawActivity::class.java)
    }

    override val layoutId: Int
        get() = R.layout.activity_shape

    override fun initView(savedInstanceState: Bundle?) {
    }

    fun onTranslate(view: View) {
        shapeView.translate(500f, 500f)
    }
}