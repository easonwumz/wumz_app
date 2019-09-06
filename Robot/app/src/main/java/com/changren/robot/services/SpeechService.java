//package com.changren.robot.services;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.annotation.IntDef;
//import android.support.annotation.Nullable;
//
//import com.aispeech.AIError;
//import com.aispeech.AIResult;
//import com.aispeech.DUILiteSDK;
//import com.aispeech.common.AIConstant;
//import com.aispeech.common.JSONResultParser;
//import com.aispeech.export.engines.AICloudASREngine;
//import com.aispeech.export.engines.AIWakeupEngine;
//import com.aispeech.export.listeners.AIASRListener;
//import com.aispeech.export.listeners.AIWakeupListener;
//import com.changren.robot.utils.AIPermissionRequest;
//import com.changren.robot.utils.CloudTTS;
//import com.changren.robot.utils.XLog;
//
///**
// * Created by Admin
// * Date 2019/3/11.
// */
//
//public class SpeechService extends Service {
//
//    private static final String TAG = SpeechService.class.getSimpleName();
//
//    private AIWakeupEngine mWakeUpEngine;
//    private AICloudASREngine mASREngine;
//
//    private static final String ACTION_TTS_CLOSE = "action_tts_close";
//
//    public static Intent getLaunchIntent(Context context){
//        return new Intent(context, SpeechService.class);
//    }
//
//    public static Intent getTTSCloseIntent(Context context){
//        Intent intent = new Intent(context, SpeechService.class);
//        intent.setAction(ACTION_TTS_CLOSE);
//        return intent;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        XLog.e(TAG, "【onCreate】");
//
//        auth();
//    }
//
//    private void auth() {
//        //设置授权连接超时时长
//        DUILiteSDK.setParameter(DUILiteSDK.KEY_AUTH_TIMEOUT, "5000");
//
//        //查询授权状态，DUILiteSDK.init()之后随时可以调
//        boolean isAuthorized = DUILiteSDK.isAuthorized(getApplicationContext());
//        XLog.e(TAG, "DUILiteSDK is isAuthorized ? " + isAuthorized);
//
//        //获取内核版本号
//        String core_version = DUILiteSDK.getCoreVersion();
//        XLog.e(TAG, "core version is: " + core_version);
//
//        //设置SDK录音模式，默认单麦模式
//        DUILiteSDK.setAudioRecorderType(DUILiteSDK.TYPE_COMMON_MIC);
//
//        //须在init之前调用.同时会保存日志文件在/sdcard/duilite/DUILite_SDK.log
//        DUILiteSDK.openLog();
//
//        DUILiteSDK.init(getApplicationContext(),
//                "6463eb2fb043af6add7c48aa5c85f4c7",  //API Key
//                "278578160",                         //产品ID
//                "9cf13cc19490dfb279c145c5d7d57ec6", //产品Key
//                "da24253ef190df06c54577f6e793b3ae",  //产品Secret
//                new DUILiteSDK.InitListener() {
//
//                    @Override
//                    public void success() {
//                        XLog.e(TAG, "【auth】【success】授权成功");
//                        initWakeUp();
//                    }
//
//                    @Override
//                    public void error(String errorCode, String errorInfo) {
//                        XLog.e(TAG, "【auth】【error】授权失败,errorCode:" + errorCode + ", errorInfoL:" + errorInfo);
//                    }
//                });
//    }
//
//    private void initWakeUp() {
//        if (!AIWakeupEngine.checkLibValid()){
//            XLog.e(TAG, "【init】so加载失败");
//        }else{
//            mWakeUpEngine = AIWakeupEngine.createInstance();
//            mWakeUpEngine.setWakeupWord(new String[]{"xiao bao xiao bao", "ni hao xiao bao", "xiao bao ni hao"}, new String[]{"0.2", "0.2", "0.2"});
//            //设置唤醒资源的名字，适用于把唤醒资源放在assets目录，须在init之前设置才生效
//            mWakeUpEngine.setResBin("wakeup_aifar_comm_20180104.bin");
//            //设置唤醒词识别校验功能，减少误唤醒
//            mWakeUpEngine.setDcheck(new String[]{"1", "1", "1"});
//            mWakeUpEngine.init(new AIWakeupListenerImpl());
//        }
//    }
//
//    private class AIWakeupListenerImpl implements AIWakeupListener {
//
//        @Override
//        public void onInit(int status) {
//            if (status == AIConstant.OPT_SUCCESS){
//                XLog.e(TAG, "【AIWakeupListenerImpl】【onInit】success");
//                startWakeUp();
//            }else{
//                XLog.e(TAG, "【AIWakeupListenerImpl】【onInit】failed, status:" + status);
//            }
//        }
//
//        @Override
//        public void onError(AIError aiError) {
//            XLog.e(TAG, "【AIWakeupListenerImpl】【onError】aiError:" + aiError.toString());
//        }
//
//        @Override
//        public void onWakeup(String recordId, double confidence, String wakeupWord) {
//            XLog.e(TAG, "【AIWakeupListenerImpl】【onWakeup】wakeupWord:" + wakeupWord + ", confidence:" + confidence);
//            CloudTTS.getInstance(SpeechService.this).speak("你好你好你好你好你好主人");
////            LocalTTS.getInstance().speak("你好主人");
//        }
//
//        @Override
//        public void onReadyForSpeech() {
//            XLog.e(TAG, "【AIWakeupListenerImpl】【onReadyForSpeech】");
//        }
//
//        @Override
//        public void onRawDataReceived(byte[] bytes, int i) {
//
//        }
//
//        @Override
//        public void onResultDataReceived(byte[] bytes, int i) {
//
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        String action = intent.getAction();
//        XLog.e(TAG, "【onStartCommand】action:" + action);
//        if (ACTION_TTS_CLOSE.equals(action)){
////            startASR();
//        }
//        return START_REDELIVER_INTENT;
//    }
//
//    private void startASR(){
//        if (!AICloudASREngine.checkLibValid()){
//            XLog.e(TAG, "【startASR】so加载失败");
//        }else{
//            if (mASREngine == null) {
//                mASREngine = AICloudASREngine.createInstance();
//                //设置识别服务器地址，若设置需要在init之前调用，默认不用设置
//                mASREngine.setServer("ws://asr.dui.ai/runtime/v2/recognize");
//                //设置是否支持标点符合识别
//                mASREngine.setEnablePunctuation(true);
//                //设置识别引擎的资源类型，取值如：comm、airobot、aihome、aicar、custom
//                mASREngine.setResourceType("aihome");
//                //设置是否启用本地VAD，一般都会打开
//                mASREngine.setLocalVadEnable(true);
//                //VAD资源名
//                mASREngine.setVadResource("vad_aihome_v0.7.bin");
//                //设置VAD pausetime，即停止说话多久表示说话结束
//                mASREngine.setPauseTime(500);
//                mASREngine.init(new AICloudASRListenerImpl());
//                //设置无语音超时时长，如果达到该设置值时，自动停止录音，设置为0表示不进行语音超时判断
//                mASREngine.setNoSpeechTimeOut(0);
//                //设置是否开启服务端的vad功能，默认开启为true
//                mASREngine.setCloudVadEnable(true);
//            }
//        }
//    }
//
//    private class AICloudASRListenerImpl implements AIASRListener{
//
//        @Override
//        public void onInit(int i) {
//            if (i == AIConstant.OPT_SUCCESS){
//                XLog.e(TAG, "【AICloudASRListenerImpl】【onInit】success");
//            }else {
//                XLog.e(TAG, "【AICloudASRListenerImpl】【onInit】failed, code:" + i);
//            }
//        }
//
//        @Override
//        public void onError(AIError aiError) {
//            XLog.e(TAG, "【AICloudASRListenerImpl】【onError】aiError:" + aiError.toString());
//        }
//
//        @Override
//        public void onResults(AIResult aiResult) {
//            if (aiResult.isLast()){
//                if (aiResult.getResultType() == AIConstant.AIENGINE_MESSAGE_TYPE_JSON){
//                    JSONResultParser parser = new JSONResultParser(aiResult.getResultObject().toString());
//                    XLog.e(TAG, "【onResults】result:" + aiResult.getResultObject().toString());
//                    XLog.e(TAG, "【onResults】result:" + parser.getText());
//                }
//            }
//        }
//
//        @Override
//        public void onRmsChanged(float v) {
//
//        }
//
//        @Override
//        public void onReadyForSpeech() {
//            XLog.e(TAG, "【AICloudASRListenerImpl】【onReadyForSpeech】请说话...");
//        }
//
//        @Override
//        public void onBeginningOfSpeech() {
//            XLog.e(TAG, "【AICloudASRListenerImpl】【onBeginningOfSpeech】检测到说话");
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            XLog.e(TAG, "【AICloudASRListenerImpl】【onEndOfSpeech】检测到语音停止，开始识别...");
//        }
//
//        @Override
//        public void onRawDataReceived(byte[] bytes, int i) {
//
//        }
//
//        @Override
//        public void onResultDataReceived(byte[] bytes, int i) {
//
//        }
//
//        @Override
//        public void onNotOneShot() {
//
//        }
//    }
//
//    private void startWakeUp(){
//        if (mWakeUpEngine != null){
//            mWakeUpEngine.start();
//        }
//    }
//
//    private void stopWakeUp(){
//        if (mWakeUpEngine != null){
//            mWakeUpEngine.stop();
//        }
//    }
//
//    private void stopASR(){
//        if (mASREngine != null){
//            mASREngine.cancel();
//        }
//    }
//
//    private void destroyWakeUp(){
//        if (mWakeUpEngine != null){
//            mWakeUpEngine.destroy();
//            mWakeUpEngine = null;
//        }
//    }
//
//    private void destroyASR(){
//        if (mASREngine != null){
//            mASREngine.destroy();
//            mASREngine = null;
//        }
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        XLog.e(TAG, "【onDestroy】");
//        stopWakeUp();
//        destroyWakeUp();
//        destroyASR();
//    }
//}
