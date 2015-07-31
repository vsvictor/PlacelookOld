package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class Logout extends BaseCommand{
	public Logout(int id){
		super(id,"user_logout");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
