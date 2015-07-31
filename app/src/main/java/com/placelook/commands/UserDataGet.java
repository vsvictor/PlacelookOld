package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDataGet extends BaseCommand{
		public UserDataGet(int id){
			super(id,"user_data_get");
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
