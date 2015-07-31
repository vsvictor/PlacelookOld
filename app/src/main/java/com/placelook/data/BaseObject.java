package com.placelook.data;

public interface BaseObject extends Comparable {
	public void setID(Object key);
	public Integer getID();
	public String getKey();
	public void setName(Object name);
	public Object getName();
}
