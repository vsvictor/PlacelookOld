package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class ListCountriesCommand extends BaseCommand{
	public ListCountriesCommand(int id){
		super(id,"list_countries");
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
