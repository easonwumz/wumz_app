package com.changren.robot.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.aispeech.dui.dds.DDS
import com.aispeech.dui.dds.DDSAuthListener
import com.aispeech.dui.dds.DDSConfig
import com.aispeech.dui.dds.DDSInitListener
import com.aispeech.dui.dds.auth.AuthType
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException
import com.changren.robot.constants.RobotConstant
import com.changren.robot.utils.XLog
import java.util.*

/**
 * Created by Admin
 * Date 2019/3/29.
 */
class DDSService : Service() {

    companion object {
        private val TAG = DDSService::class.java.simpleName

        fun getLaunchIntent(context: Context): Intent = Intent(context, DDSService::class.java)
    }

    // 授权次数,用来记录自动授权
    private var mAuthCount = 0

    override fun onCreate() {
        super.onCreate()
        XLog.e(TAG, "【onCreate】")

        init();
        object : Thread(){
            override fun run() = checkDDSReady()
        }.start()
    }

    /**
     * 检查dds是否初始成功
     */
    fun checkDDSReady(){
        while (true) {
            if (DDS.getInstance().initStatus == DDS.INIT_COMPLETE_FULL ||
                    DDS.getInstance().initStatus == DDS.INIT_COMPLETE_NOT_FULL){
                try {
                    if (DDS.getInstance().isAuthSuccess){
                        XLog.e(TAG, "【checkDDSReady】isAuthSuccess: true，and startService(DUIService)")
                        startService(DUIService.getLaunchIntent(this))
                        break
                    } else {
                        //自动授权
                        doAutoAuth()
                    }
                } catch (e: DDSNotInitCompleteException) {
                    e.printStackTrace()
                }
                break;
            }else{
                XLog.e(TAG, "【checkDDSReady】waiting init complete finish...")
            }
            try {
                Thread.sleep(800)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun doAutoAuth(){
        // 自动执行授权10次,如果10次授权失败之后,给用户弹提示框
        if (mAuthCount <10){
            try {
                DDS.getInstance().doAuth();
                mAuthCount++
            } catch (e: DDSNotInitCompleteException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 初始化DDS组件
     */
    private fun init(){
        //在调试时可以打开sdk调试日志，在发布版本时，请关闭
        DDS.getInstance().setDebugMode(2)
        DDS.getInstance().init(applicationContext,
                createConfig(),
                mInitListener,
                mAuthListener)
    }

    private fun createConfig(): DDSConfig {
        val config = DDSConfig()
        // 基础配置项
        // 产品ID -- 必填278578160
        config.addConfig(DDSConfig.K_PRODUCT_ID, "278579397")
//        config.addConfig(DDSConfig.K_PRODUCT_ID, "278578160");
        // 用户ID -- 必填
        config.addConfig(DDSConfig.K_USER_ID, "aispeech")
        // 产品的发布分支 -- 必填
        config.addConfig(DDSConfig.K_ALIAS_KEY, "prod")
//        config.addConfig(DDSConfig.K_ALIAS_KEY, "test");
        //授权方式, 支持思必驰账号授权和profile文件授权 -- 必填
        config.addConfig(DDSConfig.K_AUTH_TYPE, AuthType.PROFILE)
        // Product Key -- 必填9cf13cc19490dfb279c145c5d7d57ec6
        config.addConfig(DDSConfig.K_PRODUCT_KEY, "eea80e9c62ee5c0ae5c6504f376aa385")
//        config.addConfig(DDSConfig.K_PRODUCT_KEY, "9cf13cc19490dfb279c145c5d7d57ec6");
        // Product Secre -- 必填da24253ef190df06c54577f6e793b3ae
        config.addConfig(DDSConfig.K_PRODUCT_SECRET, "d329b5484584b49fe3bb8c5edfa8c190")
//        config.addConfig(DDSConfig.K_PRODUCT_SECRET, "da24253ef190df06c54577f6e793b3ae");
        // 产品授权秘钥，服务端生成，用于产品授权 -- 必填6463eb2fb043af6add7c48aa5c85f4c7
        config.addConfig(DDSConfig.K_API_KEY, "0b0ea2759952a6d2a2ab97e55c8a0932")
//        config.addConfig(DDSConfig.K_API_KEY, "6463eb2fb043af6add7c48aa5c85f4c7");
        //填入唯一的deviceId -- 选填
        config.addConfig(DDSConfig.K_DEVICE_ID, getDeviceId(applicationContext))
//        config.addConfig(DDSConfig.K_DEVICE_ID, "");

        // 资源更新配置项
        // 预置在指定目录下的DUI内核资源包名, 避免在线下载内核消耗流量, 推荐使用
         config.addConfig(DDSConfig.K_DUICORE_ZIP, "duicore.zip");
        // 预置在指定目录下的DUI产品配置资源包名, 避免在线下载产品配置消耗流量, 推荐使用
         config.addConfig(DDSConfig.K_CUSTOM_ZIP, "product.zip");
        // config.addConfig(DDSConfig.K_USE_UPDATE_DUICORE, "false"); //设置为false可以关闭dui内核的热更新功能，可以配合内置dui内核资源使用
        // config.addConfig(DDSConfig.K_USE_UPDATE_NOTIFICATION, "false"); // 是否使用内置的资源更新通知栏

        // 录音配置项
        // config.addConfig(DDSConfig.K_RECORDER_MODE, "internal"); //录音机模式：external（使用外置录音机，需主动调用拾音接口）、internal（使用内置录音机，DDS自动录音）
        // config.addConfig(DDSConfig.K_IS_REVERSE_AUDIO_CHANNEL, "false"); // 录音机通道是否反转，默认不反转
        // config.addConfig(DDSConfig.K_AUDIO_SOURCE, AudioSource.DEFAULT); // 内置录音机数据源类型
        // config.addConfig(DDSConfig.K_AUDIO_BUFFER_SIZE, (16000 * 1 * 16 * 100 / 1000)); // 内置录音机读buffer的大小

        // TTS配置项
        // config.addConfig(DDSConfig.K_STREAM_TYPE, AudioManager.STREAM_MUSIC); // 内置播放器的STREAM类型
        // config.addConfig(DDSConfig.K_TTS_MODE, "internal"); // TTS模式：external（使用外置TTS引擎，需主动注册TTS请求监听器）、internal（使用内置DUI TTS引擎）
        // config.addConfig(DDSConfig.K_CUSTOM_TIPS, "{\"71304\":\"请讲话\",\"71305\":\"不知道你在说什么\",\"71308\":\"咱俩还是聊聊天吧\"}"); // 指定对话错误码的TTS播报。若未指定，则使用产品配置。

        //唤醒配置项
        // config.addConfig(DDSConfig.K_WAKEUP_ROUTER, "dialog"); //唤醒路由：partner（将唤醒结果传递给partner，不会主动进入对话）、dialog（将唤醒结果传递给dui，会主动进入对话）
        // config.addConfig(DDSConfig.K_WAKEUP_BIN, "/sdcard/wakeup.bin"); //商务定制版唤醒资源的路径。如果开发者对唤醒率有更高的要求，请联系商务申请定制唤醒资源。
        // config.addConfig(DDSConfig.K_ONESHOT_MIDTIME, "500");// OneShot配置：
        // config.addConfig(DDSConfig.K_ONESHOT_ENDTIME, "2000");// OneShot配置：

        //识别配置项
        // config.addConfig(DDSConfig.K_ASR_ENABLE_PUNCTUATION, "false"); //识别是否开启标点
        // config.addConfig(DDSConfig.K_ASR_ROUTER, "dialog"); //识别路由：partner（将识别结果传递给partner，不会主动进入语义）、dialog（将识别结果传递给dui，会主动进入语义）
        // config.addConfig(DDSConfig.K_VAD_TIMEOUT, 5000); // VAD静音检测超时时间，默认8000毫秒
        // config.addConfig(DDSConfig.K_ASR_ENABLE_TONE, "true"); // 识别结果的拼音是否带音调
        // config.addConfig(DDSConfig.K_ASR_TIPS, "true"); // 识别完成是否播报提示音
        // config.addConfig(DDSConfig.K_VAD_BIN, "/sdcard/vad.bin"); // 商务定制版VAD资源的路径。如果开发者对VAD有更高的要求，请联系商务申请定制VAD资源。

        // 调试配置项
        // config.addConfig(DDSConfig.K_CACHE_PATH, "/sdcard/cache"); // 调试信息保存路径,如果不设置则保存在默认路径"/sdcard/Android/data/包名/cache"
        // config.addConfig(DDSConfig.K_WAKEUP_DEBUG, "true"); // 用于唤醒音频调试, 开启后在 "/sdcard/Android/data/包名/cache" 目录下会生成唤醒音频
        // config.addConfig(DDSConfig.K_VAD_DEBUG, "true"); // 用于过vad的音频调试, 开启后在 "/sdcard/Android/data/包名/cache" 目录下会生成过vad的音频
        // config.addConfig(DDSConfig.K_ASR_DEBUG, "true"); // 用于识别音频调试, 开启后在 "/sdcard/Android/data/包名/cache" 目录下会生成识别音频
        // config.addConfig(DDSConfig.K_TTS_DEBUG, "true");  // 用于tts音频调试, 开启后在 "/sdcard/Android/data/包名/cache/tts/" 目录下会自动生成tts音频

        // 麦克风阵列配置项
        // config.addConfig(DDSConfig.K_MIC_TYPE, "1"); // 设置硬件采集模组的类型 0：无。默认值。 1：单麦回消 2：线性四麦 3：环形六麦 4：车载双麦 5：家具双麦
        // config.addConfig(DDSConfig.K_MIC_ARRAY_AEC_CFG, "/data/aec.bin"); // 麦克风阵列aec资源的磁盘绝对路径,需要开发者确保在这个路径下这个资源存在
        // config.addConfig(DDSConfig.K_MIC_ARRAY_BEAMFORMING_CFG, "/data/beamforming.bin"); // 麦克风阵列beamforming资源的磁盘绝对路径，需要开发者确保在这个路径下这个资源存在
        // config.addConfig(DDSConfig.K_MIC_ARRAY_WAKEUP_CFG, "/data/wakeup_cfg.bin"); // 麦克风阵列wakeup配置资源的磁盘绝对路径，需要开发者确保在这个路径下这个资源存在。


        return config
    }

    private val mInitListener = object : DDSInitListener {
        override fun onInitComplete(isFull: Boolean) {
            XLog.e(TAG, "【DDSInitListener】【onInitComplete】isFull:$isFull")
            if (isFull) {
                //发送一个init成功的广播
                LocalBroadcastManager.getInstance(this@DDSService).sendBroadcast(Intent(RobotConstant.ACTION_INIT_COMPLETE))
            }
        }

        override fun onError(what: Int, msg: String?) {
            XLog.e(TAG, "【DDSInitListener】【onError】what:$what, msg:$msg")
        }

    }

    private val mAuthListener = object : DDSAuthListener {
        override fun onAuthSuccess() {
            XLog.e(TAG, "【DDSAuthListener】【onAuthSuccess】startService(DUIService)")
            startService(DUIService.getLaunchIntent(this@DDSService))
        }

        override fun onAuthFailed(errId: String?, error: String?) {
            XLog.e(TAG, "【DDSAuthListener】【onAuthFailed】")
            doAutoAuth()
        }
    }

    // 获取手机的唯一标识符: deviceId
    private fun getDeviceId(context: Context): String {
        val telephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var imei = telephonyMgr.deviceId
        var serial = Build.SERIAL
        if (TextUtils.isEmpty(imei)){
            imei = "unkown"
        }else if (TextUtils.isEmpty(serial)){
            serial = "unkown"
        }
        return UUID.nameUUIDFromBytes((imei + serial).toByteArray()).toString()
    }

    override fun onBind(intent: Intent?): IBinder? =
            throw UnsupportedOperationException("Not yet implemented")

    override fun onDestroy() {
        super.onDestroy()
        XLog.e(TAG, "【onDestroy】")
        DDS.getInstance().release()
    }
}