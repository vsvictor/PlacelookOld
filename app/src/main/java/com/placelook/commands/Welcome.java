package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victor on 22.06.15.
 */
public class Welcome extends BaseCommand{
    public Welcome(int id){
        super(id,"welcome");
        obj = new JSONObject();
        try {
            obj.put("cmd", this.getName());
            JSONObject param = new JSONObject();
            param.put("version",1);
            param.put("platform","android");
            obj.put("param", param);
            obj.put("callback", this.getName());
            obj.put("rid",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
