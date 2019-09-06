package com.changren.robot.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.changren.robot.MainActivity
import com.changren.robot.utils.XLog

/**
 * Created by Admin
 * Date 2019/4/8.
 */
class BootBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        XLog.e("BootBroadCastReceiver", "【onReceive】")
        context.startActivity(Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}