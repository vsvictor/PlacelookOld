package com.placelook.data;

public class Country extends StringStringPair{
	private int count;
	
	public Country(String id, String name, int count){
		super(id, name);
		this.count = count;
	}
	public void setCount(int count){
		this.count = count;
	}
	public int getCount(){
		return this.count;
	}
}
