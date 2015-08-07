package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

public class SlotSearchCommand extends BaseCommand{
	private int offset;
	private int limit;
	private String country;
	private int idCity;
	public SlotSearchCommand(int id, int offset, int limit, String country, int idCity){
		super(id,"slot_search");
		this.offset = offset;
		this.limit = limit;
		this.country = country;
		this.idCity = idCity;
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("offset", this.offset);
			param.put("limit", this.limit);
			param.put("country", this.country);
			param.put("id_city", this.idCity);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public SlotSearchCommand(int id, int offset, int limit, String country){
		super(id,"slot_search");
		this.offset = offset;
		this.limit = limit;
		this.country = country;
		this.idCity = idCity;
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("offset", this.offset);
			param.put("limit", this.limit);
			param.put("country", this.country);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public SlotSearchCommand(int id, int offset, int limit){
		super(id,"slot_search");
		this.offset = offset;
		this.limit = limit;
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("offset", this.offset);
			param.put("limit", this.limit);
			//param.put("country", "");
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
