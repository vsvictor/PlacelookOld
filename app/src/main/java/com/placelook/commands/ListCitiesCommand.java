package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class ListCitiesCommand extends BaseCommand{
	public ListCitiesCommand(int id){
		super(id,"list_cities");
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
	public ListCitiesCommand(int id, String country){
		super(id,"list_cities");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("country", country);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public ListCitiesCommand(int id, String country, String city, boolean findID){
		super(id,"list_cities");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			if(findID) obj.put("callback", "find_id_city");
			else obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("country", country);
			param.put("part_of_name", city);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
