package com.placelook.data;

import java.util.ArrayList;
import java.util.Collections;

public class BaseObjectList  extends ArrayList<BaseObject>{
	public BaseObject find(int id){
		BaseObject result = null;
		Integer key  = new Integer(id);
		for(BaseObject b : this){
			if(b.getID() == key) return b;
		}
		return result;
	}
	public BaseObject find(String name){
		BaseObject result = null;
		for(BaseObject b : this){
			if(b.getName().equals(name)) return b;
		}
		return result;
	}
	public boolean isPresent(String name){
		BaseObject b = find(name);
		if(b!=null) return true;
		else return false;
	}
	public void sortByName(){
		Collections.sort(this);
	}
}
