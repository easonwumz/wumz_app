package com.wu.product.base

import android.content.Context

/**
 * @Description:     description
 * @Author:         wumz
 * @CreateDate:     2019/8/30 10:59
 */
interface BaseView {

    /**
     * 显示加载框
     */
    fun showLoading()

    /**
     * 关闭加载框
     */
    fun closeLoading()

    fun showToast(msg: String)

    fun showToast(msg: Int)

    fun cancelToast()

    fun viewContext(): Context
}