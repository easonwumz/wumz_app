package com.changren.robot.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.ASREngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.changren.android.robotaiui.DDSInterface;
import com.changren.robot.RobotApplication;
import com.changren.robot.constants.RobotConstant;
import com.changren.robot.media.MediaItem;
import com.changren.robot.observer.DuiMessageObserver;
import com.changren.robot.observer.DuiUpdateObserver;
import com.changren.robot.utils.XLog;

/**
 * Created by Admin
 * Date 2019/3/13.
 * 全链路的语音service
 */
public class DUIService extends Service implements DuiUpdateObserver.UpdateCallback {

    private static final String TAG = DUIService.class.getSimpleName();

    private DuiMessageObserver mMessageObserver = new DuiMessageObserver();
//    private DuiUpdateObserver mUpdateObserver = new DuiUpdateObserver();

    //获取识别引擎
//    private ASREngine asrEngine;

    private RobotApplication app;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, DUIService.class);
    }

    private BroadcastReceiver initReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && RobotConstant.ACTION_INIT_COMPLETE.equals(action)) {
                XLog.e(TAG, "【BroadcastReceiver】【onReceive】ACTION_INIT_COMPLETE");
                enableWakeUp();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.e(TAG, "【onCreate】");

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(RobotConstant.ACTION_INIT_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(initReceiver, iFilter);

        mMessageObserver.registMessage(messageCallback);
//        mUpdateObserver.registUpdate(this);
        enableWakeUp();
        app = (RobotApplication) getApplication();
//        asrEngine = DDS.getInstance().getAgent().getASREngine();

        speak("语音交互功能已开启", 1, "1");
    }

    private DuiMessageObserver.MessageCallback messageCallback = new DuiMessageObserver.MessageCallback() {
        @Override
        public void onMessage(MediaItem item) {
            app.getPlayer().open(item);
        }

        @Override
        public void onState(String message, String state) {
            XLog.e(TAG, "【MessageCallback】【onState】message:" + message + ", state:" + state);
            if ("local_wakeup.result".equals(message)) {
                stopSpeak("100");
            }
            switch (state) {
//                case "avatar.silence":
//                    XLog.e(TAG, "【MessageCallback】avatar.silence  等待唤醒");
//                    break;
//                case "avatar.listening":
//                    XLog.e(TAG, "【MessageCallback】avatar.listening  监听中");
//                    break;
//                case "avatar.understanding":
//                    XLog.e(TAG, "【MessageCallback】avatar.understanding  理解中");
//                    break;
                case "avatar.speaking":
                    if (app.getPlayer().isPlaying()) {
                        app.stopPlayer();
                    }
                    XLog.e(TAG, "【MessageCallback】avatar.speaking  语音播放中");
                    break;
            }
        }
    };

    /**
     * 开启语音唤醒
     */
    private void enableWakeUp() {
//        Toast.makeText(getApplicationContext(), "开启唤醒", Toast.LENGTH_SHORT).show();
        try {
            DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭语音唤醒
     */
    private void disableWakeup() {
        try {
            DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取主唤醒词列表
     *
     * @return 主唤醒词的string数组
     */
    private String[] getWakeupWords() {
        try {
            return DDS.getInstance().getAgent().getWakeupEngine().getWakeupWords();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 供外部app调用tts的方法
     * @param content 播报内容
     * @param priority 优先级
     *  提供4个优先级播报。
     * ①优先级0：保留，与DDS语音交互同级，仅限内部使用；
     * ②优先级1：正常，默认选项，同级按序播放；
     * ③优先级2：重要，可以插话<优先级1>，同级按序播放，播报完毕后继续播报刚才被插话的<优先级1>；
     * ④优先级3：紧急，可以打断当前正在播放的<优先级1|优先级2>，同级按序播放，播报完毕后播报剩余的<优先级2|优先级1>。
     * 4）audioFocus 该次播报的音频焦点，默认值:
     *  ①优先级0：android.media.AudioManager#AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE
     *  ②优先级非0：android.media.AudioManager#AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
     */
    private void speak(String content, int priority, String ttsId) {
        try {
            stopSpeak("0");
            DDS.getInstance().getAgent().getTTSEngine().speak(content, priority, ttsId,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止语音播报
     * 1）ttsId与speak接口的ttsId一致，则停止或者移除该播报；
     * 2）ttsId为空， 停止所有播报；
     * 3）ttsId为"0"，停止当前播报。
     * @param ttsId tts id
     */
    private void stopSpeak(String ttsId){
        try {
            DDS.getInstance().getAgent().getTTSEngine().shutup(ttsId);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if ("com.changren.robot.DUIService.action".equals(intent.getAction())) {
            return new TTSBinder();
        }
        return null;
    }

    private class TTSBinder extends DDSInterface.Stub {

        @Override
        public void speak(String content) throws RemoteException {
            try {
                DDS.getInstance().getAgent().stopDialog();
                Thread.sleep(100);
            } catch (DDSNotInitCompleteException | InterruptedException e) {
                e.printStackTrace();
            }
            DUIService.this.speak(content, 1, "100");
        }

        @Override
        public void shutUp() throws RemoteException {
            stopSpeak("100");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XLog.e(TAG, "【onDestroy】");
        mMessageObserver.unRegistMessage();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(initReceiver);
    }

    @Override
    public void onUpdate(int type, String result) {
        Looper.prepare();
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        Looper.loop();
        XLog.e(TAG, "【onUpdate】result：" + result);
    }
}
