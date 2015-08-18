package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victor on 11.08.15.
 */
public class SessionAbuse extends BaseCommand {
    public SessionAbuse(int id, int idSession, int idAbuse, String msg){
        super(id,"session_abuse");
        obj = new JSONObject();
        try {
            obj.put("cmd", this.getName());
            obj.put("callback", this.getName());
            obj.put("rid", this.getID());
            JSONObject param = new JSONObject();
            param.put("id_session", idSession);
            param.put("id_abuse_code", idAbuse);
            param.put("msg", msg);
            obj.put("param", param);
            setText(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
