package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateSlotCommand extends BaseCommand {
	public CreateSlotCommand(int id, String country, int idCity, double latitude, double longitude, boolean isGPS){
		super(id,"slot_create");
		obj = new JSONObject();
		String sIDCity;
		if(idCity == -1){
			sIDCity = "";
		}
		else{
			sIDCity = String.valueOf(idCity);
		}
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("country", country);
			param.put("id_city", sIDCity);
			param.put("width", latitude);
			param.put("height", longitude);
			param.put("gps", isGPS);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
