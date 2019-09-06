package com.wu.product.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import com.blankj.utilcode.util.ToastUtils
import com.kaopiz.kprogresshud.KProgressHUD

/**
 * @Description: description
 * @Author: wumz
 * @CreateDate: 2019/8/30 10:47
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {

    protected lateinit var unBinder: Unbinder
    protected lateinit var mKProgressHUD: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        unBinder = ButterKnife.bind(this)
        initView(savedInstanceState)
    }

    protected abstract val layoutId: Int

    protected abstract fun initView(savedInstanceState: Bundle?)

    override fun showLoading() {
        mKProgressHUD = KProgressHUD.create(this)
        mKProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
                .show()
    }

    override fun closeLoading() {
        if (mKProgressHUD.isShowing) {
            mKProgressHUD.dismiss()
        }
    }

    override fun showToast(msg: String) {
        ToastUtils.showShort(msg)
    }

    override fun showToast(msg: Int) {
        ToastUtils.showShort(msg)
    }

    override fun cancelToast() {
        ToastUtils.cancel()
    }

    override fun onStop() {
        super.onStop()
        cancelToast()
    }

    override fun viewContext(): Context {
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        unBinder.unbind()
    }
}
