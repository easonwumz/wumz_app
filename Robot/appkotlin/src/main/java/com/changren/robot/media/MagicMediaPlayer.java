package com.changren.robot.media;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.util.Log;

import com.changren.robot.RobotApplication;

import java.io.IOException;

public class MagicMediaPlayer implements IMediaPlayer, OnPreparedListener,
		OnCompletionListener {
	public static final int FLAG_PLAYBACK_ONE = 0;
	public static final int FLAG_PLAYBACK_SHUFFLE = 1;
	public static final int FLAG_PLAYBACK_BYTITLE = 2;
	public static final int FLAG_PLAYBACK_BYARTIST = 3;
	private MediaPlayer mInternalPlayer;
	private PlaylistManager mPL;
	private MediaItem mDataSource;
	private RobotApplication mAPP;

	public MagicMediaPlayer(RobotApplication app) {
		mAPP = app;
		mInternalPlayer = new MediaPlayer();
		mInternalPlayer.setOnPreparedListener(this);
		mInternalPlayer.setOnCompletionListener(this);
		mPL = PlaylistManager.getInstance();
	}

	@Override
	public void setDataSource(MediaItem item) throws IOException,
			IllegalArgumentException, SecurityException, IllegalStateException {
		// TODO Auto-generated method stub
		mInternalPlayer.setDataSource(item.getPath());
	}

	@Override
	public void prepareAsync() throws IOException, IllegalArgumentException,
			SecurityException, IllegalStateException {
		// TODO Auto-generated method stub
		mInternalPlayer.prepareAsync();
	}

	@Override
	public void prepare() throws IOException {
		// prepare-->prepareAsync
		mInternalPlayer.prepareAsync();

	}

	@Override
	public void stop() throws IllegalStateException {
		mInternalPlayer.stop();
		mDataSource = null;
	}

	@Override
	public void start() {
		if (!mInternalPlayer.isPlaying())
			mInternalPlayer.start();

	}

	@Override
	public void pause() {
		if (mInternalPlayer.isPlaying()) {
			mInternalPlayer.pause();
		}

	}

	@Override
	public void next() {
		// TODO Auto-generated method stub

		int currentIndex = mDataSource == null ? 0 : mPL
				.findByContent(mDataSource);

		currentIndex++;
		if (currentIndex >= mPL.getSize())
			currentIndex = 0;
		open(currentIndex);
	}

	@Override
	public void previous() {
		// TODO Auto-generated method stub

		int currentIndex = mDataSource == null ? 0 : mPL
				.findByContent(mDataSource);
		currentIndex--;

		if (currentIndex < 0)
			currentIndex = mPL.getSize() - 1;

		open(currentIndex);
	}

	@Override
	public void open(int index) {
		// TODO Auto-generated method stub
		mDataSource = mPL.findByIndex(index);

		if (mInternalPlayer.isPlaying()) {
			mInternalPlayer.stop();
			mInternalPlayer.reset();
		}
		open(mDataSource.getPath(), mDataSource.isLocalFile());
	}

	public void open(MediaItem item) {

		if (item == null)
			item = mPL.findByIndex(0);
		mDataSource = item;
		if (mInternalPlayer.isPlaying()) {
			mInternalPlayer.stop();
			mInternalPlayer.reset();
		}
//		if (item instanceof OnlineMediaItem)
//			OnlineContentManager.getInstance(mAPP).updateHistory(true,
//					(OnlineMediaItem) item);
		open(item.getPath(), !item.isLocalFile());
	}
	@Override
	public void saveHistory(MediaItem item) {
		// TODO Auto-generated method stub
//		if (item instanceof OnlineMediaItem)
//			OnlineContentManager.getInstance(mAPP).updateHistory(true,
//					(OnlineMediaItem) item);
	}

	private void open(String path, boolean isLocal) {
		open(path, true, isLocal);
	}

	private void open(String path, boolean async, boolean isLocal) {

		Log.d("REJIN",
				"datasource:" + path + ",currentTime:"
						+ System.currentTimeMillis());

		try {
			mInternalPlayer.reset();
			mInternalPlayer.setDataSource(path);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mInternalPlayer.reset();
			return;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mInternalPlayer.reset();
			return;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mInternalPlayer.reset();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mInternalPlayer.reset();
			return;
		}
		if (async) {
			try {
				prepareAsync();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mInternalPlayer.reset();
				return;
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mInternalPlayer.reset();
				return;
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mInternalPlayer.reset();
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mInternalPlayer.reset();
				return;
			}
		} else {
			try {
				mInternalPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mInternalPlayer.reset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mInternalPlayer.reset();
			}
			mInternalPlayer.start();
		}

	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return mInternalPlayer.isPlaying();
	}

	@Override
	public void reset() {
		mInternalPlayer.reset();
	}

	@Override
	public void release() {
		mInternalPlayer.release();
		mInternalPlayer = null;
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return mInternalPlayer.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		return mInternalPlayer.getCurrentPosition();
	}

	@Override
	public void seekTo(int msec) throws IllegalStateException {
		// TODO Auto-generated method stub
		mInternalPlayer.seekTo(msec);
	}

	@Override
	public void setOnCompletionListener(OnCompletionListener listener) {
		// TODO Auto-generated method stub
		mInternalPlayer.setOnCompletionListener(listener);
	}

	@Override
	public void setOnErrorListener(OnErrorListener listener) {
		// TODO Auto-generated method stub
		mInternalPlayer.setOnErrorListener(listener);
	}

	@Override
	public void setOnInfoListener(OnInfoListener listener) {
		// TODO Auto-generated method stub
		mInternalPlayer.setOnInfoListener(listener);
	}

	@Override
	public void setOnPreparedListener(OnPreparedListener listener) {
		// TODO Auto-generated method stub
		mInternalPlayer.setOnPreparedListener(listener);
	}

	@Override
	public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
		// TODO Auto-generated method stub
		mInternalPlayer.setOnSeekCompleteListener(listener);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// wumz 修改 注释了next()
		// next();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub

		mInternalPlayer.start();

	}

	@Override
	public MediaItem getDataSource() {
		// TODO Auto-generated method stub
		return mDataSource;
	}

}
