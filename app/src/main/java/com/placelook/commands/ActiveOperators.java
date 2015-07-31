package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class ActiveOperators extends BaseCommand{
	public ActiveOperators(int id){
		super(id,"activity_operators");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			obj.put("param", param);
			setText(obj.toString());
			int i = 0;
			i++;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
