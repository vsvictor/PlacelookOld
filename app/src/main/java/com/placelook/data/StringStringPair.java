package com.placelook.data;

import com.placelook.data.BaseObject;

import java.util.Comparator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class StringStringPair implements BaseObject {
	private int id;
	private String name;
	private String key;
	public StringStringPair(String key, String name) {
		this.id = -1;
		this.key = key;
		this.name = name;
	}
	@Override
	public void setID(Object key) {
		this.key = (String) key;
	}
	@Override
	public Integer getID() {
		return id;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setName(Object name) {
		this.name = (String) name;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public int compareTo(Object another) {
		return this.getName().compareToIgnoreCase((String) ((BaseObject)another).getName());
	}
}
