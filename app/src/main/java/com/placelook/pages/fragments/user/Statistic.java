package com.placelook.pages.fragments.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.placelook.MainActivity;
import com.placelook.R;
import com.placelook.data.BaseObjectList;
import com.placelook.data.FilteredAdapter;
import com.placelook.data.StatCard;
import com.placelook.data.StatisticList;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.pages.fragments.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by victor on 13.08.15.
 */
public class Statistic extends BaseFragment {
    private BaseObjectList list;
    private StatisticList statis;
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        list = new BaseObjectList();
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.stat, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        //MainActivity.getHelper().userStatistic(0, 1000);
    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter statFilter = new IntentFilter();
        statFilter.addAction("user_stats");
        MainActivity.getMainActivity().registerReceiver(stat, statFilter);
    }
    @Override
    public void onPause(){
        super.onPause();
        MainActivity.getMainActivity().unregisterReceiver(stat);
    }
    BroadcastReceiver stat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getExtras().getString("command");
            try {
                list.clear();
                JSONObject obj = new JSONObject(command);
                JSONObject data = obj.getJSONObject("param");
                int total = data.getInt("total");
                int list_len = data.getInt("list_length");
                JSONObject arr = data.getJSONObject("list");
                Iterator keys = arr.keys();
                while(keys.hasNext()){
                    String key = (String) keys.next();
                    JSONObject item = arr.getJSONObject(key);
                    StatCard card = new StatCard(Integer.parseInt(key));
                    card.setRole(item.getString("type"));
                    card.setDate(item.getLong("ut_created"));
                    card.setBalance(item.getInt("balance"));
                    list.add(card);
                }
                Collections.sort(list);
                statis = restruct(list);
                int i = 0;
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private StatisticList restruct(BaseObjectList data){
        StatisticList result = new StatisticList(data);
        return result;
    }
    private class StatAdapter extends FilteredAdapter{

        public StatAdapter(BaseObjectList list) {
            super(list);
        }
        public StatAdapter(Context context, BaseObjectList list) {
            super(context, list);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
