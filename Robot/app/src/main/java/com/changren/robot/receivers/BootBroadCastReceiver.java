package com.changren.robot.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.changren.robot.services.DDSService;

/**
 * Created by Admin
 * Date 2019/3/12.
 */

public class BootBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootBroadCastReceiver", "【onReceive】");
//        context.startService(SpeechService.getLaunchIntent(context));
        context.startService(DDSService.getLaunchIntent(context));
    }
}
