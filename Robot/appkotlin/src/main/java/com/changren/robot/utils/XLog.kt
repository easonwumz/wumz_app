package com.changren.robot.utils

import android.util.Log

/**
 * Created by Admin
 * Date 2019/3/29.
 */
object XLog {

    private var deBug = true
    private val TAG = "Wu"

    fun deBug(debug: Boolean){
        deBug = debug
    }

    fun d(tag: String, o: Any){
        if (deBug){
            Log.e(TAG, "【" + tag +  "】" + o.toString())
        }
    }

    fun e(tag: String, o: Any){
        if (deBug){
            Log.e(TAG, "【" + tag + "】" + o.toString())
        }
    }
}