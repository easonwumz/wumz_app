package com.changren.robot.utils;

import android.util.Log;

/**
 * XLog
 * Created by Wu on 2018/1/5.
 */
public class XLog {

    private static boolean deBug = true;
    private static final String TAG = "Wu";

    public static void deBug(boolean debug){
        deBug = debug;
    }

    public static void d(String tag, Object o){
        if (deBug){
            Log.d(TAG, "【" + tag + "】" + o.toString());
        }
    }

    public static void e(String tag, Object o){
        if (deBug){
            Log.e(TAG, "【" + tag + "】" + o.toString());
        }
    }
}
