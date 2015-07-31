package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class SlotConfirmCommand extends BaseCommand{

	public SlotConfirmCommand(int id, int idSlot){
		super(id,"slot_confirm");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("id_slot", idSlot);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
