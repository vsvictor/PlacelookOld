package com.placelook.commands;

import android.provider.Settings;

import com.placelook.Account;
import com.placelook.utils.Security;

import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends BaseCommand{
	public Registration(int id, Account acc){
		super(id,"user_register");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("email", acc.getLogin());
			param.put("password", acc.getPassword());
			param.put("anonymous", acc.isTemporary()?"1":"0");
			param.put("code", Security.md5(Settings.Secure.ANDROID_ID));
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public Registration(int id, String sLogin, String sPassword){
		super(id,"user_register");
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("email", sLogin);
			param.put("password", sPassword);
			param.put("anonymous", "0");
			param.put("code", Security.md5(Settings.Secure.ANDROID_ID));
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
