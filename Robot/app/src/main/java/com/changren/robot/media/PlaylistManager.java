package com.changren.robot.media;

import java.util.ArrayList;



public class PlaylistManager{

	private static PlaylistManager mInstance;
	private ArrayList<MediaItem> mPlayList = new ArrayList<MediaItem>();

	public static PlaylistManager getInstance()
	{
		if(mInstance == null)
			mInstance = new PlaylistManager();
		
		return mInstance;
	}
	
	public synchronized void  createNewPlaylist(ArrayList<MediaItem> pl)
	{
		mPlayList = pl;
	}
	
	public synchronized ArrayList<MediaItem> getPlaylist()
	{
		return mPlayList;
	}
	
	public int findByContent(MediaItem item)
	{
		return mPlayList.indexOf(item);
	}
	
	public MediaItem findByIndex(int index)
	{
		return mPlayList.get(index);
	}
	
	public int getSize()
	{
		return mPlayList.size();
	}

}
