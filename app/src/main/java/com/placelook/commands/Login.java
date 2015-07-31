package com.placelook.commands;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import com.placelook.Account;
import com.placelook.MainActivity;
//import com.placelook.NetService;

public class Login extends BaseCommand {
	private String login;
	private String password;
	public Login(int id, String login, String password, boolean autoLogin){
		super(id,"user_login");
		this.login = login;
		this.password = password;
        obj = new JSONObject();
        try {
			obj.put("cmd", this.getName());
			obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("email", login);
			param.put("password", password);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public Login(int id, Account acc, boolean autoLogin){
		super(id, "user_login");
		this.login = acc.getLogin();
		this.password = acc.getPassword();
		obj = new JSONObject();
		try {
			obj.put("cmd", this.getName());
			if(autoLogin){obj.put("callback", "user_autologin");}
			else obj.put("callback", this.getName());
			obj.put("rid", this.getID());
			JSONObject param = new JSONObject();
			param.put("email", login);
			param.put("password", password);
			obj.put("param", param);
			setText(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
