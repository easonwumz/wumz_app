//package com.changren.robot.utils;
//
//
//import android.content.Context;
//import android.media.AudioManager;
//
//import com.aispeech.AIError;
//import com.aispeech.common.AIConstant;
//import com.aispeech.export.engines.AICloudTTSEngine;
//import com.aispeech.export.listeners.AITTSListener;
//import com.changren.robot.services.SpeechService;
//
///**
// * Created by Admin
// * Date 2019/3/11.
// */
//
//public class CloudTTS {
//
//    private static final String TAG = CloudTTS.class.getSimpleName();
//
//    private static CloudTTS instance = null;
//
//    private AICloudTTSEngine mEngine;
//    private Context mContext;
//
//    private boolean isSpeaking = false;
//
//    public static CloudTTS getInstance(Context context){
//        if (instance == null){
//            synchronized(CloudTTS.class){
//                if (instance == null){
//                    instance = new CloudTTS(context);
//                }
//            }
//        }
//        return instance;
//    }
//
//    private CloudTTS(Context context){
//        mContext = context.getApplicationContext();
//    }
//
//    private void initEngine(final String content){
//        mEngine = AICloudTTSEngine.createInstance();
//        //设置合成的文本类型，默认位text
//        mEngine.setTextType("text");
//        //访问云端合成服务器地址，默认为该地址
//        mEngine.setServer("http://tts.dui.ai/runtime/v2/synthesize");
//        //设置是否使用本地缓存，默认为true。最大换成20条合成音频
//        mEngine.setUseCache(false);
//        //设置合成音播放的音频流，默认为音乐流
//        mEngine.setStreamType(AudioManager.STREAM_MUSIC);
//        mEngine.setVolume("100");
//        mEngine.setSpeaker("linbaf_qingxin");
//        mEngine.init(new AITTSListener() {
//            @Override
//            public void onInit(int i) {
//                if (i == AIConstant.OPT_SUCCESS){
//                    XLog.e(TAG, "【initEngine】【onInit】success");
//                    speak(content);
//                }else {
//                    XLog.e(TAG, "【initEngine】【onInit】failed");
//                }
//            }
//
//            @Override
//            public void onError(String utteranceId, AIError error) {
//                XLog.e(TAG, "【initEngine】【onError】onError:" + utteranceId + ", error:" + error.toString());
//                isSpeaking = false;
//                int code = error.getErrId();
//                if (70911 == code){
//                    //网络错误，走本地语音合成
//                    LocalTTS.getInstance(mContext).speak(content);
//                }else if (72203 == code){
//                    XLog.e(TAG, "【initEngine】【onError】无效的合成文本");
//                }else if (70917 == code){
//                    XLog.e(TAG, "【initEngine】【onError】合成引擎还未初始化");
//                }else if (70918 == code){
//                    XLog.e(TAG, "【initEngine】【onError】播放队列已满");
//                }
//            }
//
//            @Override
//            public void onReady(String s) {
//                XLog.e(TAG, "【initEngine】【onReady】" + s);
//                isSpeaking = true;
//            }
//
//            @Override
//            public void onCompletion(String s) {
//                //云端语音合成结束
//                XLog.e(TAG, "【initEngine】【onCompletion】" + s);
//                isSpeaking = false;
//                mContext.startService(SpeechService.getTTSCloseIntent(mContext));
//            }
//
//            @Override
//            public void onProgress(int i, int i1, boolean isRefTextTTSFinished) {
//            }
//        });
//        //设置合成音的保存路径
////        mEngine.setAudioPath(Environment.getExternalStorageDirectory() + "/DUI/tts");
//    }
//
//    public void speak(String content) {
//        if (mEngine == null) {
//            initEngine(content);
//        } else {
//            if (isSpeaking) {
//                stopTTS();
//            }
//            if (mEngine != null) {
//                mEngine.speak(content, "1024");
//            }
//        }
//    }
//
//    /**
//     * 停止音频合成
//     */
//    private void stopTTS(){
//        if (mEngine != null){
//            mEngine.stop();
//        }
//    }
//
//    /**
//     * 暂停音频合成
//     */
//    private void pauseTTS(){
//        if (mEngine != null){
//            mEngine.pause();
//        }
//    }
//
//    /**
//     * 恢复音频合成
//     */
//    private void resumeTTS(){
//        if (mEngine != null){
//            mEngine.resume();
//        }
//    }
//
//    private void release(){
//        if (mEngine != null){
//            mEngine.stop();
//            mEngine.release();
//        }
//    }
//}
