package com.placelook.pages.fragments.operator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.placelook.Constants;
import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.pages.MainPage;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.pages.fragments.OnClick;
import com.placelook.pages.fragments.operator.WarningContinue;
import com.placelook.pages.fragments.operator.WarningText;
import com.placelook.video.RecordActivity;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bytedeco.javacpp.presets.opencv_core;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by victor on 14.07.15.
 */
public class Operator extends BaseFragment {
    private WarningText wt;
    private WarningContinue wc;
    private WaitingClient ww;
    private WaitingSettings ws;
    private WaitingMinimize wm;
    private Incomming inc;
    private int slot;
    private int sec;
    private String goal;
    private Logger log;
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        slot = -1;
        wt = new WarningText();
        wc = new WarningContinue();
        ww = new WaitingClient();
        ws = new WaitingSettings();
        wm = new WaitingMinimize();
        try {
            slot = getArguments().getInt("slot");
            sec = getArguments().getInt("time");
            goal = getArguments().getString("goal");
        } catch (Exception e) {

        }
        log = createLogger();
    }

    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.operator, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        if (slot == -1) {
            getFragmentManager().beginTransaction().
                    add(R.id.rlWarningTextOperator, wt).
                    add(R.id.rlWarningContinueOperator, wc).
                    remove(MainPage.getFooter()).
                    commit();
        } else {
            Bundle args = new Bundle();
            args.putInt("slot", slot);
            args.putInt("time", sec);
            args.putString("goal", goal);
            inc.setArguments(args);
            getFragmentManager().beginTransaction().
                    add(R.id.llOperator, inc).
                    remove(MainPage.getFooter()).
                    commit();
        }
        wc.setOnContinueListener(new OnClick() {
            @Override
            public void onClick() {
                getFragmentManager().beginTransaction().
                        remove(wt).
                        remove(wc).
                        add(R.id.rlWaitingClientOperator, ww).
                        add(R.id.rlWaitingSettingOperator, ws).
                        add(R.id.rlWaitingMinimizeOperator, wm).
                        commit();
            }
        });
        ws.setOnWaitingSetting(new OnWaitingSettingListener() {
            @Override
            public void sound(boolean b) {
                SharedPreferences sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sh.edit();
                ed.putBoolean("sound", b);
                ed.commit();
            }

            @Override
            public void vibrate(boolean b) {
                SharedPreferences sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sh.edit();
                ed.putBoolean("vibrate", b);
                ed.commit();
            }

            @Override
            public void popupWindow(boolean b) {
                SharedPreferences sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sh.edit();
                ed.putBoolean("popup", b);
                ed.commit();
            }
        });
        wm.setOnMinimize(new OnClick() {
            @Override
            public void onClick() {
                Intent i = new Intent(MainActivity.getMainActivity(), Slotter.class);
                i.putExtra("accuracy", Criteria.ACCURACY_MEDIUM);
                MainActivity.getMainActivity().startService(i);
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter ifConf = new IntentFilter();
        ifConf.addAction("slot_confirm");
        MainActivity.getMainActivity().registerReceiver(rec, ifConf);
        IntentFilter ifOper = new IntentFilter();
        ifOper.addAction("operator");
        MainActivity.getMainActivity().registerReceiver(oper, ifOper);
        IntentFilter clSlotReq = new IntentFilter();
        clSlotReq.addAction("client_slot_request");
        MainActivity.getMainActivity().registerReceiver(clReq, clSlotReq);
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.getMainActivity().unregisterReceiver(rec);
        MainActivity.getMainActivity().unregisterReceiver(oper);
    }

    BroadcastReceiver clReq = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getExtras().getString("command");

            try {
                JSONObject obj = new JSONObject(command);
                JSONObject param = obj.getJSONObject("param");
                slot = param.getInt("id_slot");
                sec = param.getInt("duration_sec");
                goal = param.getString("description");
                inc = new Incomming();
                inc.setOnAccept(new OnAcceptListener() {
                    @Override
                    public void onAccept(int slot) {
                        MainActivity.getMainActivity().getHelper().slotConfirm(slot);
                    }
                    @Override
                    public void onReject(int slot) {
                    }
                });
                Bundle args = new Bundle();
                args.putInt("slot", slot);
                args.putInt("time", sec);
                args.putString("goal", goal);
                inc.setArguments(args);
                getFragmentManager().beginTransaction().
                        remove(ww).
                        remove(ws).
                        remove(wm).
                        add(R.id.llOperator, inc).
                        commit();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();;
                log.info(e.getMessage());
            }
        }
    };
    BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getExtras().getString("command");
            try {
                JSONObject obj = new JSONObject(command);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    BroadcastReceiver oper = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getExtras().getString("command");
            try {
                JSONObject obj = new JSONObject(command);
                JSONObject param = obj.getJSONObject("param");
                int id = param.getInt("id");
                String role = param.getString("role");
                String url = param.getString("url");
                Intent in = new Intent(MainActivity.getMainActivity(), RecordActivity.class);
                in.putExtra("idSession", id);
                in.putExtra("role", role);
                in.putExtra("url", url);
                MainActivity.getMainActivity().startActivityForResult(in, 1);
                //int i = 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Logger createLogger(){
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                + File.separator + "placelook.txt");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ALL);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
        Logger logger = Logger.getLogger(String.valueOf(MainActivity.class));
        return logger;
    }

}
