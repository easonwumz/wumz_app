//package com.changren.robot.utils;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.text.TextUtils;
//
//import com.aispeech.AIError;
//import com.aispeech.common.AIConstant;
//import com.aispeech.export.engines.AILocalTTSEngine;
//import com.aispeech.export.listeners.AILocalTTSListener;
//
///**
// * Created by Admin
// * Date 2019/3/11.
// */
//
//public class LocalTTS {
//
//    private static final String TAG = LocalTTS.class.getSimpleName();
//
//    private static LocalTTS instance = null;
//
//    private AILocalTTSEngine mEngine;
//    private Context mContext;
//
//    private boolean isSpeaking = false;
//
//    public static LocalTTS getInstance(Context context){
//        if (instance == null){
//            synchronized(CloudTTS.class){
//                if (instance == null){
//                    instance = new LocalTTS(context);
//                }
//            }
//        }
//        return instance;
//    }
//
//    private LocalTTS(Context context){
//        mContext = context.getApplicationContext();
//    }
//
//    private void initEngine(String content) {
//        if (mEngine != null){
//            mEngine.destroy();
//        }
//        mEngine = AILocalTTSEngine.createInstance();
//        //设置assets目录下的合成前端资源名字
//        mEngine.setFrontResBin("local_front.bin", "local_front.bin.md5sum");
//        //设置assets目录下的合成字典名字和对应的md5文件名
//        mEngine.setDictDb("aitts_sent_dict_idx_2.0.4_20180806.db", "aitts_sent_dict_idx_2.0.4_20180806.db.md5sum");
//        //设置assets目录下的后端发音人资源名和对应的md5文件，初始化时默认以第一个资源名加载进内核，若只需要一个发音人，则设置一个即可
//        mEngine.setBackResBinArray(new String[]{"zhilingf_common_back_ce_local.v2.1.0.bin"}, new String[]{"zhilingf_common_back_ce_local.v2.1.0.bin.md5sum"});
//        //初始化合成引擎
//        mEngine.init(new AILocalTTSListenerImpl(content));
//        mEngine.setStreamType(AudioManager.STREAM_MUSIC);
//        //设置是否使用ssml语法来播报文本，若设置需要在start之前调用
//        mEngine.setUseSSML(false);
//        mEngine.setSpeechVolume(100);
//    }
//
//    private class AILocalTTSListenerImpl implements AILocalTTSListener{
//        String content;
//
//        AILocalTTSListenerImpl(String content){
//            this.content = content;
//        }
//
//        @Override
//        public void onInit(int status) {
//            if (status == AIConstant.OPT_SUCCESS){
//                XLog.e(TAG, "【AILocalTTSListenerImpl】【onInit】success");
//                speak(content);
//            }else {
//                XLog.e(TAG, "【AILocalTTSListenerImpl】【onInit】failed, code:" + status);
//            }
//        }
//
//        @Override
//        public void onError(String s, AIError aiError) {
//            XLog.e(TAG, "【AILocalTTSListenerImpl】【onError】aiError:" + aiError.toString());
//            isSpeaking = false;
//        }
//
//        @Override
//        public void onSynthesizeStart(String s) {
//            XLog.e(TAG, "【AILocalTTSListenerImpl】【onSynthesizeStart】合成开始");
//        }
//
//        @Override
//        public void onSynthesizeDataArrived(String s, byte[] bytes) {
////            XLog.e(TAG, "【AILocalTTSListenerImpl】【onSynthesizeStart】");
//        }
//
//        @Override
//        public void onSynthesizeFinish(String s) {
//            XLog.e(TAG, "【AILocalTTSListenerImpl】【onSynthesizeFinish】合成结束");
//            isSpeaking = false;
//        }
//
//        @Override
//        public void onSpeechStart(String s) {
//            XLog.e(TAG, "【AILocalTTSListenerImpl】【onSpeechStart】开始播放");
//            isSpeaking = true;
//        }
//
//        @Override
//        public void onSpeechProgress(int i, int i1, boolean b) {
//
//        }
//
//        @Override
//        public void onSpeechFinish(String s) {
//            XLog.e(TAG, "【AILocalTTSListenerImpl】【onSpeechFinish】播放完成");
//            isSpeaking = false;
//        }
//    }
//
//    public void speak(String content){
//        if (!TextUtils.isEmpty(content)) {
//            if (!AILocalTTSEngine.checkLibValid()){
//                XLog.e(TAG, "【init】so加载失败");
//            }else {
//                if (mEngine != null) {
//                    mEngine.speak(content, "1024");
//                }else{
//                    initEngine(content);
//                }
//            }
//        }else{
//            XLog.e(TAG, "【speak】合成文本不合法");
//        }
//    }
//
//    /**
//     * 暂停播放
//     */
//    private void pauseTTS(){
//        if (mEngine != null){
//            mEngine.pause();
//        }
//    }
//
//    /**
//     * 恢复播放
//     */
//    private void resumeTTS(){
//        if (mEngine != null){
//            mEngine.resume();
//        }
//    }
//
//    /**
//     * 停止播放
//     */
//    private void stopTTS(){
//        if (mEngine != null){
//            mEngine.stop();
//        }
//    }
//
//    private void destroyTTS(){
//        if (mEngine != null){
//            mEngine.destroy();
//            mEngine = null;
//        }
//    }
//}
