package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionCloseCommand extends BaseCommand{
	public SessionCloseCommand(int id, int idSession){
		super(id, "session_close");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("id_session", idSession);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
