package com.changren.robot;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.changren.robot.services.DDSService;
import com.changren.robot.services.DUIService;
import com.changren.robot.utils.AIPermissionRequest;
import com.changren.robot.utils.XLog;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.functions.Action1;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AIPermissionRequest mPermissionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XLog.e(TAG, "【onCreate】");

        stopService(DDSService.getLaunchIntent(this));
        stopService(DUIService.getLaunchIntent(this));
        initRequest();
//        startService(DDSService.getLaunchIntent(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
        XLog.e(TAG, "【onResume】");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XLog.e(TAG, "【onDestroy】");
    }

    private void initRequest() {
        mPermissionRequest = new AIPermissionRequest();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            requestMulit();
//        }else{
//            startService(DDSService.getLaunchIntent(this));
//            finish();
//        }

        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean){
                            XLog.e(TAG, "【initRequest】【call】全部权限已打开");
                            startService(DDSService.getLaunchIntent(getApplicationContext()));
//                            finish();
                        }else{
                            XLog.e(TAG, "【initRequest】【call】权限未全部打开");
                        }
                    }
                });

    }

    private void requestMulit() {
        mPermissionRequest.requestMultiPermissions(this, mPermissionGrant);
    }

    private AIPermissionRequest.PermissionGrant mPermissionGrant = new AIPermissionRequest.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            XLog.e(TAG, "【AIPermissionRequest】【onPermissionGranted】requestCode:" + requestCode);
            switch (requestCode){
                case AIPermissionRequest.CODE_READ_PHONE_STATE:
                    break;
                case AIPermissionRequest.CODE_RECORD_AUDIO:
                    break;
                case AIPermissionRequest.CODE_READ_EXTERNAL_STORAGE:
                    break;
                case AIPermissionRequest.CODE_WRITE_EXTERNAL_STORAGE:
                    break;
                case AIPermissionRequest.CODE_READ_CONTACTS:
                    break;
                default:
                    break;
            }
        }
    };
}
