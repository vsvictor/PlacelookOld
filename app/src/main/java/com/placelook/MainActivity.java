package com.placelook;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.placelook.commands.*;
import com.placelook.commands.Error;
import com.placelook.pages.MainPage;
import com.placelook.pages.fragments.Claim;
import com.placelook.pages.fragments.EndSession;
import com.placelook.pages.fragments.OnClaim;
import com.placelook.pages.fragments.OnEndSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;

import de.mindpipe.android.logging.log4j.LogConfigurator;


public class MainActivity extends Activity {
    private static final String TAG = "MainAvtivity";
    private static MainActivity instance;
    private static MainPage mp;
    private EndSession es;
    private Claim claim;
    private Account acc;
    private static Logger log;
    private static Placelook helper;
    private boolean active;
    private NetService netService;
    public static String last_command;
    private int errCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setBackground(getResources().getDrawable(R.drawable.rect_blue));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);
        instance = this;
        log = createLogger();
        acc = new Account(this);
        acc.load();
        helper = new Placelook(this);
        mp = new MainPage(this);
        getFragmentManager().beginTransaction().add(R.id.llMain, mp).commit();
        Intent in = new Intent(this, NetService.class);
        in.putExtra("obj", "start");
        bindService(new Intent(this, NetService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        }, BIND_AUTO_CREATE);
        helper.welcome();
    }
    @Override
    public void onResume(){
        super.onResume();
        claim = new Claim();
        es = new EndSession();
        es.setOnEndSession(new OnEndSession() {
            @Override
            public void onShowAlign() {
            }
            @Override
            public void onMainScreen() {
                getFragmentManager().beginTransaction().
                        replace(R.id.llMain, MainActivity.mp).
                        add(R.id.rlHeaderFragment, MainPage.getHeader()).
                        add(R.id.rlMainFragment, MainPage.getMain()).
                        add(R.id.rlFooterFragment, MainPage.getFooter()).
                        commit();
            }
            @Override
            public void onClaimToClient() {
                Bundle b = new Bundle();
                b.putString("role","operator");
                claim.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.rlMainFragment,claim).commit();
            }
            @Override
            public void onClaimToOperator() {
                Bundle b = new Bundle();
                b.putString("role","client");
                claim.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.rlMainFragment,claim).commit();
            }
        });

        claim.setOnClaim(new OnClaim() {
            @Override
            public void onClaim(boolean falseGoal, boolean tryOperator, boolean behaviaor, boolean req, String opt) {
                getFragmentManager().beginTransaction().
                        replace(R.id.llMain, MainActivity.mp).
                        add(R.id.rlHeaderFragment, MainPage.getHeader()).
                        add(R.id.rlMainFragment, MainPage.getMain()).
                        add(R.id.rlFooterFragment, MainPage.getFooter()).
                        commit();
            }
        });
        IntentFilter ifFirst = new IntentFilter();
        ifFirst.addAction("start");
        registerReceiver(first, ifFirst);

        IntentFilter ifWelc = new IntentFilter();
        ifWelc.addAction("welcome");
        registerReceiver(welc, ifWelc);


        IntentFilter startFilter = new IntentFilter();
        //startFilter.addAction("welcome");
        startFilter.addAction("user_login");
        startFilter.addAction("user_register");
        registerReceiver(startReceiver, startFilter);

        IntentFilter errFilter = new IntentFilter();
        errFilter.addAction("error");
        registerReceiver(errorer, errFilter);

    }
    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(errorer);
        unregisterReceiver(startReceiver);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(this, NetService.class);
        intent.putExtra("obj", "stop");
        startService(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, null);
        String sRole = data.getExtras().getString("role");
        Bundle b = new Bundle();
        b.putString("role", sRole);
        es.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.rlMainFragment, es).commit();
    }
    public static MainActivity getMainActivity(){
        return instance;
    }
    public Account getAccount(){
        if(acc == null) {acc = new Account(this);acc.load();}
        return acc;
    }
    public static Placelook getHelper(){return helper;}
    private BroadcastReceiver first = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            JSONObject obj;
            try {
                obj = new JSONObject(intent.getExtras().getString("command"));
                int status = obj.getInt("status_code");
                if(status == 1 && (obj.getString("callback").equals("start"))){
                        log.info("started");
                        helper.welcome();
                        unregisterReceiver(first);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private BroadcastReceiver welc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            JSONObject obj;
            try {
                obj = new JSONObject(intent.getExtras().getString("command"));
                int status = obj.getInt("status_code");
                if(status == 1 && (obj.getString("callback").equals("welcome"))){
                    helper.login();
                    unregisterReceiver(welc);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private BroadcastReceiver errorer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            errCounter++;
            String st = intent.getExtras().getString("command");
            if(errCounter>3){
                errCounter = 0;
                try {
                    JSONObject obj = new JSONObject(st);
                    int code = obj.getInt("status_code");
                    if(code == 3) {
                        getHelper().registration();
                        return;
                    }
                    Error err = new Error(getMainActivity(),code);
                    err.setTextCommand(st);
                    Error.Critical cl = err.view();
                    if(cl == Error.Critical.MAIN){
                        getFragmentManager().beginTransaction().
                                replace(R.id.llMain, MainActivity.mp).
                                add(R.id.rlHeaderFragment, MainPage.getHeader()).
                                add(R.id.rlMainFragment, MainPage.getMain()).
                                add(R.id.rlFooterFragment, MainPage.getFooter()).
                                commit();
                        getHelper().welcome();
                    }
                    else if(cl == Error.Critical.EXIT){
                        Intent in = new Intent(instance,NetService.class);
                        in.putExtra("obj", "stop");
                        instance.startService(in);
                        finish();
                        System.exit(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Intent sec = new Intent(getMainActivity(),NetService.class);
                sec.putExtra("obj",MainActivity.last_command);
                getMainActivity().startService(sec);
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
    public Logger getLogger(){
        return log;
    }


    private BroadcastReceiver startReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            JSONObject obj;
            try {
                obj = new JSONObject(intent.getExtras().getString("command"));
                int status = obj.getInt("status_code");
                /*if((status == 1) && (obj.getString("callback").equals("welcome"))){
                    helper.login();
                }
                else*/ if((status == 1) && (obj.getString("callback").equals("user_login"))){
                    getAccount().login(true);
                    JSONObject param = obj.getJSONObject("param");
                    int userID = param.getInt("id_user");
                    boolean anonim = param.getInt("anonymous")==0?true:false;
                    getAccount().setUserID(userID);
                    getAccount().setTypeAccount(anonim);
                    if(helper.sLog != null && helper.sPas != null){
                        getAccount().setLogin(helper.sLog);
                        getAccount().setEmail(helper.sLog);
                        getAccount().setPassword(helper.sPas);
                        helper.sLog = null;
                        helper.sPas = null;
                    }
                    if(helper.bSave) getAccount().save();
                    helper.user_data_get();
                    helper.active_operators();
                }
                else if((status == 3) && (obj.getString("callback").equals("user_login"))) {
                    helper.registration();
                }
                else if((status == 1) && (obj.getString("callback").equals("user_register"))) {
                    helper.login();
                }
                else if((status == 1) && (obj.getString("callback").equals("user_logout"))) {
                    //getAccount().setTypeAccount(false);
                    //getAccount().login(false);
                    //getAccount().loadTemp();
                    //helper.welcome();
                }
                else{
                    log.info(TAG+":"+"Error: "+obj.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
