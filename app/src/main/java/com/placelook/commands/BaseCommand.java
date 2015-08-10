package com.placelook.commands;

import android.content.Context;
import android.content.Intent;

//import com.placelook.IOService;
//import com.placelook.NetService;
import com.placelook.MainActivity;
import com.placelook.NetService;
import com.placelook.Placelook;
import com.placelook.data.BaseObject;
import com.placelook.data.IntStringPair;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseCommand extends IntStringPair{
	private Integer id;
	protected String name;
	protected String text;
	protected JSONObject obj;
	public BaseCommand(){
		super(0,"");
		this.id = 0;
		this.name = name;
	}
	public BaseCommand(int id, String name){
		super(id, name);
		this.id = id;
		this.name = name;
	}
	public BaseCommand(int id, String name, String text){
		this(id, name);
		this.text = text;
	}
	public void setText(String text){
		this.text = text;
	}
	public String getText(){return this.text;}
	boolean inActions(String action){
		ArrayList<String> arr = Actions.toArrayList();
		for(String act : arr){
			if(action.equals(act)) return true;
		}
		return false;
	}
	public String toString(){
		return obj.toString();
	}
	public JSONObject toJSONObject(){
		return obj;
	}
	public void send(Context context){
		Intent intent = new Intent(context, NetService.class);
		String s = this.toString();
		intent.putExtra("obj", s);
		context.startService(intent);
		MainActivity.last_command = s;
	}
	public static class Fields{
		public static String message = "message";
	}
}
