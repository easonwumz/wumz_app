package com.changren.robot.media;

import java.io.Serializable;

public class ContentItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String id = "-1";
	protected String path;
	protected String title;
	
	public ContentItem(String id,String path,String title)
	{
		this.id = id;
		this.path = path;
		this.title = title;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(o.getClass() != this.getClass())
		    return false;
		
		ContentItem item = (ContentItem)o;
		if(id.equals(item.getId()) && title.equals(item.getTitle()) && path.equals(item.getPath()))
			return true;
		
		return false;
	}
}
