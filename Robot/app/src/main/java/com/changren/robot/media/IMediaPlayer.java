package com.changren.robot.media;

import java.io.IOException;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;


public interface IMediaPlayer {
	
	public void setDataSource(MediaItem item)throws IOException, 
	IllegalArgumentException, SecurityException, IllegalStateException;
	
	public void prepareAsync()throws IOException, 
	IllegalArgumentException, SecurityException, IllegalStateException;
	
	public void prepare()throws IOException;
	
	public void stop() throws IllegalStateException;
	
	public void start();
	
	public void pause();
	
	public void next();
	
	public void previous();
	
	public boolean isPlaying();
	
	public int getDuration();

	public void reset();
	
	public void release();
	
	public void open(int index);
	
	public void saveHistory(MediaItem item);
	public void open(MediaItem item);
	
	public int getCurrentPosition();
	
	public MediaItem getDataSource();
	
	public void seekTo(int msec) throws IllegalStateException;
	
	public void setOnCompletionListener(OnCompletionListener listener);
	
	public void setOnErrorListener(OnErrorListener listener);
	
	public void setOnInfoListener(OnInfoListener listener);
	
	public void setOnPreparedListener(OnPreparedListener listener);
	
	public void setOnSeekCompleteListener(OnSeekCompleteListener listener);


}
