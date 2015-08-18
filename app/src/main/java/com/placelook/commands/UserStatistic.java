package com.placelook.commands;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victor on 13.08.15.
 */
public class UserStatistic  extends BaseCommand{
    public UserStatistic(int id, int offset, int limit){
        super(id,"user_stats");
        obj = new JSONObject();
        try {
            obj.put("cmd", this.getName());
            obj.put("callback", this.getName());
            obj.put("rid", this.getID());
            JSONObject param = new JSONObject();
            param.put("offset",offset);
            param.put("limit", limit);
            obj.put("param", param);
            setText(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
