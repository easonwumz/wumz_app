package com.changren.robot.observer;

import android.content.Context;
import android.text.TextUtils;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;
import com.changren.robot.RobotApplication;
import com.changren.robot.media.MediaItem;
import com.changren.robot.utils.XLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin
 * Date 2019/3/14.
 */

public class DuiMessageObserver implements MessageObserver {

    private static final String TAG = DuiMessageObserver.class.getSimpleName();

    private String[] mSubscribeKeys = new String[]{
            "local_wakeup.result",
            "sys.dialog.state",
            "context.output.text",
            "context.input.text",
            "context.widget.content",
            "context.widget.list",
            "context.widget.web",
            "context.widget.media"};

    private MessageCallback mMessageCallback;

    public interface MessageCallback{
        void onMessage(MediaItem item);

        void onState(String message, String state);
    }

    /**
     * 注册当前更新消息
     * @param messageCallback MessageCallback
     */
    public void registMessage(MessageCallback messageCallback){
        this.mMessageCallback = messageCallback;
        DDS.getInstance().getAgent().subscribe(mSubscribeKeys, this);
    }

    /**
     * 注销当前更新消息
     */
    public void unRegistMessage(){
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    @Override
    public void onMessage(String message, String data) {
        XLog.e(TAG, "【onMessage】message:" + message + ", data:" + data);
        switch (message){
            case "local_wakeup.result":
                XLog.e(TAG, "【onMessage】local_wakeup.result  收到唤醒");
                if (mMessageCallback != null){
                    mMessageCallback.onState(message, data);
                }
                break;
            case "context.output.text":
                break;
            case "context.input.text":
                break;
            case "context.widget.content":
                break;
            case "context.widget.list":
                break;
            case "context.widget.web":
                break;
            case "context.widget.media":
                try {
                    JSONObject jo = new JSONObject(data);
                    JSONArray contentArray = jo.getJSONArray("content");
                    if (contentArray != null) {
                        JSONObject contentObj = (JSONObject) contentArray.get(0);
                        if (contentObj != null) {
                            String url = contentObj.getString("linkUrl");
                            if (TextUtils.isEmpty(url)) {
                                return;
                            }
                            mMessageCallback.onMessage(new MediaItem(null, url, null));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "sys.dialog.state":
                if (mMessageCallback != null){
                    mMessageCallback.onState(message, data);
                }
                break;
        }
    }
}
