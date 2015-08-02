package com.placelook.pages;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.placelook.MainActivity;
import com.placelook.R;
import com.placelook.pages.fragments.Footer;
import com.placelook.pages.fragments.Header;
import com.placelook.pages.fragments.Main;
import com.placelook.pages.fragments.OnClick;
import com.placelook.pages.fragments.client.Client;
import com.placelook.pages.fragments.operator.Operator;
import com.placelook.pages.fragments.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victor on 20.06.15.
 */
public class MainPage extends BasePage {
    private static Header header;
    private static Main main;
    private static Footer footer;
    private static Operator op;
    private static User us;
    private static Client cl;
    public MainPage(){
        super(MainActivity.getMainActivity());
        header = new Header();
        main = new Main();
        footer = new Footer();
    }
    @SuppressLint("ValidFragment")
    public MainPage(MainActivity act){
        super(act);
        header = new Header();
        main = new Main();
        footer = new Footer();
    }
    @SuppressLint("ValidFragment")
    public MainPage(MainActivity act, BasePage prev){
        super(act);
        this.prev = prev;
        header = new Header();
        main = new Main();
        footer = new Footer();
    }
    @Override
    public void onCreate(Bundle saved){
        super.onCreate(saved);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rootView = inf.inflate(R.layout.main_page, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        Intent in = MainActivity.getMainActivity().getIntent();
        try{
            String s = in.getExtras().getString("command");
            JSONObject obj = new JSONObject(s);
            JSONObject param = obj.getJSONObject("param");
            int sec = param.getInt("duration_sec");
            String goal = param.getString("description");
            int slot = param.getInt("id_slot");
            Operator op = new Operator();
            Bundle args = new Bundle();
            args.putInt("slot", slot);
            args.putInt("time", sec);
            args.putString("goal", goal);
            op.setArguments(args);
            getFragmentManager().beginTransaction().
                    add(R.id.rlHeaderFragment, header).
                    add(R.id.rlMainFragment, op).
                    add(R.id.rlFooterFragment, footer).
                    commit();
        } catch (Exception e) {
            getFragmentManager().beginTransaction().
                    add(R.id.rlHeaderFragment, header).
                    add(R.id.rlMainFragment, main).
                    add(R.id.rlFooterFragment, footer).
                    commit();
        }
        header.setOnUserClickListener(new OnClick() {
            @Override
            public void onClick() {
                us = new User();
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.rlMainFragment, us).commit();
            }
        });
        header.setOnHeaderClickListener(new OnClick() {
            @Override
            public void onClick() {
            }
        });
        main.setOnShow(new OnClick() {
            @Override
            public void onClick() {
                //Toast.makeText(MainActivity.getMainActivity(), "Show", Toast.LENGTH_LONG).show();
                op = new Operator();
                getFragmentManager().beginTransaction().
                        replace(R.id.rlMainFragment, op).
                        commit();
            }
        });
        main.setOnView(new OnClick() {
            @Override
            public void onClick() {
                cl = new Client();
                getFragmentManager().beginTransaction().
                        replace(R.id.rlMainFragment, cl).
                        commit();
            }
        });
        footer.setOnAddBalanceListener(new OnClick() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.getMainActivity(), "Add balance", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        header.setInvisibleAll();
        header.setVisibleUser(true);
        IntentFilter fil = new IntentFilter();
        fil.addAction("client_slot_request");
        MainActivity.getMainActivity().registerReceiver(rec, fil);
    }
    @Override
    public void onPause(){
        super.onPause();
        MainActivity.getMainActivity().unregisterReceiver(rec);
    }
    public static Header getHeader(){return header;}
    public static Footer getFooter(){
        //if(footer == null) footer = new Footer();
        return footer;
    }
    public static Main getMain(){
        return main;
    }
    BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getExtras().getString("command");
            try {
                JSONObject obj = new JSONObject(command);
                int slot = obj.getInt("id_slot");
                int sec = obj.getInt("duration_sec");
                String goal = obj.getString("description");
                Operator us = new Operator();
                Bundle b = new Bundle();
                b.putInt("slot", slot);
                b.putInt("time", sec);
                b.putString("goal", goal);
                us.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.rlMainFragment, us).commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
