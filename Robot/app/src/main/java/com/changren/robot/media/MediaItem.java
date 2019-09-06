package com.changren.robot.media;

public class MediaItem extends ContentItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2059757947959736780L;
	
	protected boolean mIsLocal = true;
	
	public MediaItem(String id, String path, String title) {
		this(id, path, title,false);
		// TODO Auto-generated constructor stub
	}
	
	public MediaItem(String id, String path, String title,boolean isLocal) {
		super(id, path, title);
		// TODO Auto-generated constructor stub
		this.mIsLocal = isLocal;
	}
	
	public boolean isLocalFile()
	{
		return mIsLocal;
	}


}
