package com.changren.robot.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.IBinder
import android.os.Looper
import android.os.RemoteException
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.widget.Toast
import com.aispeech.dui.dds.DDS
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException
import com.changren.android.robotaiui.DDSInterface
import com.changren.robot.RobotApplication
import com.changren.robot.constants.RobotConstant
import com.changren.robot.media.MediaItem
import com.changren.robot.observer.DuiMessageObserver
import com.changren.robot.observer.DuiUpdateObserver
import com.changren.robot.utils.XLog

/**
 * Created by Admin
 * Date 2019/3/29.
 */
class DUIService : Service(), DuiUpdateObserver.UpdateCallback {

    companion object {
        private val TAG = DUIService::class.java.simpleName

        fun getLaunchIntent(context: Context): Intent = Intent(context, DUIService::class.java)
    }

    private val mMessageObserver = DuiMessageObserver()
//    private val mUpdateObserver = DuiUpdateObserver()

    private var app: RobotApplication? = null

    private val initReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (!TextUtils.isEmpty(action) && RobotConstant.ACTION_INIT_COMPLETE == action){
                XLog.e(TAG, "【BroadcastReceiver】【onReceive】ACTION_INIT_COMPLETE")
                enableWakeUp()
            }
        }
    }

    private val messageCallback = object : DuiMessageObserver.MessageCallback {
        override fun onMessage(item: MediaItem) {
            if (app != null)
                app!!.player.open(item)
        }

        override fun onState(message: String, state: String) {
            XLog.e(TAG, "【MessageCallback】【onState】message:$message, state:$state")
            if (message == "local_wakeup.result"){
                stopSpeak("100");
            }
            when(state){
                "avatar.speaking" -> {
                    if (app != null && app!!.player.isPlaying) {
                        app!!.stopPlayer()
                    }
                    XLog.e(TAG, "【MessageCallback】avatar.speaking  语音播放中")
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        XLog.e(TAG, "【onCreate】")

        val iFilter = IntentFilter()
        iFilter.addAction(RobotConstant.ACTION_INIT_COMPLETE)
        LocalBroadcastManager.getInstance(this).registerReceiver(initReceiver, iFilter)

        mMessageObserver.registMessage(messageCallback)
//        mUpdateObserver.registUpdate(this)
        enableWakeUp()
        app = application as RobotApplication?
        speak("语音交互功能已开启", 1, "1")
    }

    /**
     * 开启语音唤醒
     */
    private fun enableWakeUp() = try {
        DDS.getInstance().agent.wakeupEngine.enableWakeup()
    } catch (e: DDSNotInitCompleteException) {
        e.printStackTrace()
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    }

    /**
     * 关闭语音唤醒
     */
    private fun disableWakeup() = try {
        DDS.getInstance().agent.wakeupEngine.disableWakeup()
    } catch (e: DDSNotInitCompleteException) {
        e.printStackTrace()
    }

    /**
     * 获取主唤醒词列表
     *
     * @return 主唤醒词的string数组
     */
    private val wakeupWords: Array<String>?
        get(){
            try {
                return DDS.getInstance().agent.wakeupEngine.wakeupWords
            } catch (e: DDSNotInitCompleteException) {
                e.printStackTrace()
            }
            return null
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
    private fun speak(content: String, priority: Int, ttsId: String) = try {
        stopSpeak("0");
        DDS.getInstance().agent.ttsEngine.speak(content, priority, ttsId,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
    } catch (e: DDSNotInitCompleteException) {
        e.printStackTrace()
    }

    /**
     * 停止语音播报
     * 1）ttsId与speak接口的ttsId一致，则停止或者移除该播报；
     * 2）ttsId为空， 停止所有播报；
     * 3）ttsId为"0"，停止当前播报。
     * @param ttsId tts id
     */
    private fun stopSpeak(ttsId: String) = try {
        DDS.getInstance().agent.ttsEngine.shutup(ttsId);
    }catch (e: DDSNotInitCompleteException) {
        e.printStackTrace()
    }

    override fun onUpdate(type: Int, result: String) {
        Looper.prepare()
        Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
        Looper.loop()
        XLog.e(TAG, "【onUpdate】result:$result")
    }

    override fun onBind(intent: Intent): IBinder? =
            if ("com.changren.robot.DUIService.action" == intent.action){
                TTSBinder()
            }else null

    private inner class TTSBinder : DDSInterface.Stub() {
        override fun shutUp() = stopSpeak("100")

        @Throws(RemoteException::class)
        override fun speak(content: String) {
            try {
                DDS.getInstance().agent.stopDialog()
                Thread.sleep(100)
            } catch (e: DDSNotInitCompleteException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            this@DUIService.speak(content, 1, "100")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        XLog.e(TAG, "【onDestroy】")
        mMessageObserver.unRegistMessage()
//        mUpdateObserver.unRegistUpdate()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(initReceiver)
    }
}