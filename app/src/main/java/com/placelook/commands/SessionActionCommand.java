package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionActionCommand  extends BaseCommand{

	public SessionActionCommand(int id, int idSession, String command){
		super(id, "session_action");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("id_session", String.valueOf(idSession));
			if(inActions(command)){
				param.put("type", command);
			}
			else{
				param.put("type", Actions.TEXT);
				param.put("msg", command);
			}
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public SessionActionCommand(int id, int idSession, String command, String msg){
		super(id, "session_action");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("id_session", String.valueOf(idSession));
			param.put("type", Actions.TEXT);
			param.put("msg", msg);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public SessionActionCommand(int id, int idSession, double width, double height){
		super(id, "session_action");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("id_session", String.valueOf(idSession));
			param.put("type", Actions.GEO);
			param.put("width", String.valueOf(width));
			param.put("height", String.valueOf(height));
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
