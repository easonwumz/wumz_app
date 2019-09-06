package com.changren.robot.observer

import android.text.TextUtils
import com.aispeech.dui.dds.DDS
import com.aispeech.dui.dds.agent.MessageObserver
import com.changren.robot.media.MediaItem
import com.changren.robot.utils.XLog
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Admin
 * Date 2019/3/29.
 */
class DuiMessageObserver : MessageObserver{

    companion object {
        private val TAG = DuiMessageObserver::class.java.simpleName
    }

    private val mSubscribeKeys = arrayOf(
            "local_wakeup.result",
            "sys.dialog.state",
            "context.output.text",
            "context.input.text",
            "context.widget.content",
            "context.widget.list",
            "context.widget.web",
            "context.widget.media")

    private var mMessageCallback: MessageCallback? = null

    interface MessageCallback {
        fun onMessage(item: MediaItem)

        fun onState(message: String, state: String)
    }

    /**
     * 注册当前更新消息
     * @param messageCallback MessageCallback
     */
    fun registMessage(messageCallback: MessageCallback){
        mMessageCallback = messageCallback
        try {
            DDS.getInstance().agent.subscribe(mSubscribeKeys, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 注销当前更新消息
     */
    fun unRegistMessage() = DDS.getInstance().agent.unSubscribe(this)

    override fun onMessage(message: String, data: String) {
        XLog.e(TAG, "【onMessage】message:$message, data:$data")
        when(message) {
            "local_wakeup.result" -> {
                XLog.e(TAG, "【onMessage】local_wakeup.result  收到唤醒")
                if (mMessageCallback != null){
                    mMessageCallback!!.onState(message, data)
                }
            }
            "context.output.text" -> {
            }
            "context.input.text" -> {
            }
            "context.widget.content" -> {
            }
            "context.widget.list" -> {
            }
            "context.widget.web" -> {
            }
            "context.widget.media" -> try {
                val jo = JSONObject(data)
                val contentArray = jo.getJSONArray("content")
                if (contentArray != null) {
                    val contentObj = contentArray.get(0) as JSONObject
                    val url = contentObj.getString("linkUrl")
                    if (TextUtils.isEmpty(url)) {
                        return
                    }
                    mMessageCallback!!.onMessage(MediaItem(null, url, null))
                }
            }catch (e: JSONException){
                e.printStackTrace()
            }
            "sys.dialog.state" -> if (mMessageCallback != null){
                    mMessageCallback!!.onState(message, data)
                }
        }
    }
}