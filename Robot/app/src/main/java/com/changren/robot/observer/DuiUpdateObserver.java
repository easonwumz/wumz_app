package com.changren.robot.observer;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.dds.update.DDSUpdateListener;
import com.changren.robot.utils.XLog;

/**
 * Created by Admin
 * Date 2019/3/14.
 */

public class DuiUpdateObserver implements MessageObserver {

    private static final String TAG = DuiUpdateObserver.class.getSimpleName();

    private static final int START = 0; // 开始更新
    private static final int UPDATEING = 1; // 正在更新
    private static final int FINISH = 2;// 更新完成
    private static final int ERROR = 3;// 更新失败

    private UpdateCallback mUpdateCallback;

    public interface UpdateCallback{
        void onUpdate(int type, String result);
    }

    /**
     * 注册当前更新消息
     * @param updateCallback UpdateCallback
     */
    public void registUpdate(UpdateCallback updateCallback){
        this.mUpdateCallback = updateCallback;
        DDS.getInstance().getAgent().subscribe("sys.resource.updated", this);
        initUpdate();
    }

    /**
     * 注销当前更新消息
     */
    public void unRegistUpdate(){
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    /**
     * 初始化更新
     */
    private void initUpdate(){
        try {
            DDS.getInstance().getUpdater().update(ddsUpdateListener);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String message, String data) {
        initUpdate();
    }

    private DDSUpdateListener ddsUpdateListener = new DDSUpdateListener() {
        @Override
        public void onUpdateFound(String s) {
            XLog.e(TAG, "【DDSUpdateListener】【onUpdateFound】detail:" + s);
            if (mUpdateCallback != null){
                mUpdateCallback.onUpdate(START, "发现新版本");
            }
//            try {
//                DDS.getInstance().getAgent().getTTSEngine().speak("发现新版本，正在为您更新", 1);
//            } catch (DDSNotInitCompleteException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onUpdateFinish() {
            if (mUpdateCallback != null){
                mUpdateCallback.onUpdate(FINISH, "更新成功");
            }
            // 更新成功后不要立即调用speak提示用户更新成功, 这个时间DDS正在初始化
        }

        @Override
        public void onDownloadProgress(float v) {
            XLog.e(TAG, "【DDSUpdateListener】【onDownloadProgress】正在更新:" + v + " / 100");
//            if (mUpdateCallback != null){
//                mUpdateCallback.onUpdate(UPDATEING, "正在更新:" + v + " / 100");
//            }
        }

        @Override
        public void onError(int i, String s) {
            if (mUpdateCallback != null){
                mUpdateCallback.onUpdate(ERROR, "更新失败");
            }
            XLog.e(TAG, "【DDSUpdateListener】【onError】what:" + i + ", error:" + s);
        }

        @Override
        public void onUpgrade(String s) {
            if (mUpdateCallback != null){
                mUpdateCallback.onUpdate(ERROR, "更新失败，当前sdk版本过低，和dui平台上的dui内核不匹配，请更新sdk");
            }
        }
    };
}
