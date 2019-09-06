package com.changren.robot.observer

import com.aispeech.dui.dds.DDS
import com.aispeech.dui.dds.agent.MessageObserver
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException
import com.aispeech.dui.dds.update.DDSUpdateListener
import com.changren.robot.utils.XLog

/**
 * Created by Admin
 * Date 2019/3/29.
 */
class DuiUpdateObserver : MessageObserver {

    companion object {
        private val TAG = DuiUpdateObserver::class.java.simpleName

        private val START = 0        // 开始更新
        private val UPDATEING = 1    // 正在更新
        private val FINISH = 2       // 更新完成
        private val ERROR = 3        // 更新失败
    }

    private var mUpdateCallback: UpdateCallback? = null

    interface UpdateCallback {
        fun onUpdate(type: Int, result: String)
    }

    /**
     * 注册当前更新消息
     * @param updateCallback UpdateCallback
     */
    fun registUpdate(updateCallback: UpdateCallback){
        mUpdateCallback = updateCallback
        DDS.getInstance().agent.subscribe("sys.resource.updated", this)
        initUpdate()
    }

    /**
     * 注销当前更新消息
     */
    fun unRegistUpdate(){
        DDS.getInstance().agent.unSubscribe(this)
    }

    /**
     * 初始化更新
     */
    private fun initUpdate() = try {
        DDS.getInstance().updater.update(ddsUpdateListener)
    } catch (e: DDSNotInitCompleteException) {
        e.printStackTrace()
    }

    override fun onMessage(message: String, data: String) = initUpdate()

    private val ddsUpdateListener = object : DDSUpdateListener {

        override fun onUpdateFound(s: String) {
            XLog.e(TAG, "【DDSUpdateListener】【onUpdateFound】detail:$s")
            if (mUpdateCallback != null){
                mUpdateCallback!!.onUpdate(START, "发现新版本")
            }
        }

        override fun onUpgrade(s: String) {
            if (mUpdateCallback != null) {
                mUpdateCallback!!.onUpdate(ERROR, "更新失败，当前sdk版本过低，和dui平台上的dui内核不匹配，请更新sdk")
            }
        }

        override fun onUpdateFinish() {
            if (mUpdateCallback != null) {
                mUpdateCallback!!.onUpdate(FINISH, "更新成功")
            }
            // 更新成功后不要立即调用speak提示用户更新成功, 这个时间DDS正在初始化
        }

        override fun onError(i: Int,s: String) {
            if (mUpdateCallback != null) {
                mUpdateCallback!!.onUpdate(ERROR, "更新失败")
            }
            XLog.e(TAG, "【DDSUpdateListener】【onError】what:$i, error:$s")
        }

        override fun onDownloadProgress(v: Float) {
            XLog.e(TAG, "【DDSUpdateListener】【onDownloadProgress】正在更新:$v / 100")
//            if (mUpdateCallback != null){
//                mUpdateCallback!!.onUpdate(UPDATEING, "正在更新:$v / 100");
//            }
        }
    }
}