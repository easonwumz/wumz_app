package com.changren.robot;

import android.app.Application;

import com.changren.robot.media.IMediaPlayer;
import com.changren.robot.media.MagicMediaPlayer;
import com.changren.robot.utils.XLog;

/**
 * Created by Admin
 * Date 2019/3/11.
 */

public class RobotApplication extends Application {

    private IMediaPlayer mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        XLog.deBug(false);
    }

    public IMediaPlayer getPlayer(){
        if (mPlayer == null){
            mPlayer = new MagicMediaPlayer(this);
        }
        return mPlayer;
    }

    public IMediaPlayer stopPlayer(){
        if (mPlayer != null && mPlayer.isPlaying()){
            mPlayer.stop();
            mPlayer = null;
        }

        return mPlayer;
    }

    @Override
    public void onTerminate() {
        if (mPlayer != null){
            mPlayer.release();
        }
        super.onTerminate();
    }
}
