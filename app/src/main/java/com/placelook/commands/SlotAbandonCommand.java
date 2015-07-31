package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class SlotAbandonCommand extends BaseCommand{

	public SlotAbandonCommand(int id, int idSlot){
		super(id,"slot_abandon");
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
