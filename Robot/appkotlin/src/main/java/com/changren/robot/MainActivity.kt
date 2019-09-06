package com.changren.robot

import android.Manifest
import android.app.Activity
import android.os.Bundle
import com.changren.robot.services.DDSService
import com.changren.robot.services.DUIService
import com.tbruyelle.rxpermissions.RxPermissions
import com.changren.robot.utils.XLog

class MainActivity : Activity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        XLog.e(TAG, "【onCreate】")

        stopService(DDSService.getLaunchIntent(this))
        stopService(DUIService.getLaunchIntent(this))

        initPermissions()
    }

    override fun onResume() {
        super.onResume()
        finish()
        XLog.e(TAG, "【onResume】")
    }

    private fun initPermissions(){
        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe { t ->
                    if (t!!){
                        XLog.e(TAG, "【initPermissions】【call】全部权限已打开")
                        startService(DDSService.getLaunchIntent(applicationContext))
                    }else{
                        XLog.e(TAG, "【initPermissions】【call】权限未全部打开")
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        XLog.e(TAG, "【onDestroy】")
    }
}
