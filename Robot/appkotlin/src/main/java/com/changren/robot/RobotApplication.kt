package com.changren.robot

import android.app.Application
import com.changren.robot.media.IMediaPlayer
import com.changren.robot.media.MagicMediaPlayer
import com.changren.robot.utils.XLog

/**
 * Created by Admin
 * Date 2019/3/29.
 */
class RobotApplication : Application() {

    private var mPlayer: IMediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        XLog.deBug(true)
    }

    val player: IMediaPlayer
        get(){
            if (mPlayer == null) {
                mPlayer = MagicMediaPlayer(this)
            }
            return mPlayer as IMediaPlayer
        }

    fun stopPlayer(): IMediaPlayer? {
        if (mPlayer != null && mPlayer!!.isPlaying) {
            mPlayer!!.stop()
            mPlayer = null
        }
        return mPlayer
    }

    override fun onTerminate() {
        if (mPlayer != null){
            mPlayer!!.release()
        }
        super.onTerminate()
    }
}
