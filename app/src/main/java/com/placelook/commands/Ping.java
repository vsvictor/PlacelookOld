package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class Ping extends BaseCommand{
	public Ping(int id){
		super(id,"ping");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", "pong");
			obj.put("rid", "");
			JSONObject param = new JSONObject();
			param.put("time",System.currentTimeMillis());
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
