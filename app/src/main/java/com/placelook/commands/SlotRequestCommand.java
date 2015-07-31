package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class SlotRequestCommand extends BaseCommand{
	public SlotRequestCommand(int id, int idOperator, int timeInMinutes, String goal){
		super(id,"slot_request");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("id_slot", idOperator);
			param.put("description", goal);
			param.put("duration_sec", timeInMinutes);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
