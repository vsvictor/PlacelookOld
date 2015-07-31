package com.placelook.data;

public class IntStringPair implements BaseObject{
	protected int id;
	protected String name;

	public IntStringPair(){
		id = 0;
		name = "";
	}
	public IntStringPair(int id, String name){
		this.id = id;
		this.name = name;
	}
	@Override
	public void setID(Object key) {
		this.id = (Integer) key;
	}
	@Override
	public Integer getID(){
		return id;
	}

	@Override
	public String getKey() {
		return String.valueOf(getID());
	}

	@Override
	public void setName(Object name) {
		this.name = (String) name;
	}
	@Override
	public String getName(){
		return name;
	}
	@Override
	public int compareTo(Object another) {
		return this.getName().compareToIgnoreCase((String) ((BaseObject)another).getName());
	}
    public String toJSONElement(){
    	return String.valueOf(getID())+":"+getName();
    }
}
