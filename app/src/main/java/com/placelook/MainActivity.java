package com.placelook;

import android.app.Activity;
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

import com.placelook.commands.BaseCommand;
import com.placelook.commands.Login;
import com.placelook.pages.MainPage;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;

import de.mindpipe.android.logging.log4j.LogConfigurator;


public class MainActivity extends Activity {
    private static final String TAG = "MainAvtivity";
    private static MainActivity instance;
    private MainPage mp;
    private Account acc;
    private static Logger log;
    private Placelook helper;
    private boolean active;
    private NetService netService;
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
        //startService(in);
        //netService = new NetService();
        //netService.bi

        bindService(new Intent(this, NetService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //helper.welcome();
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
        if(resultCode == RESULT_OK){
            String sRole = data.getExtras().getString("role");
            if(sRole.equals("operator")){
                //this.switchFragment(new EndSessionPage(), false);
            }
            else if(sRole.equals("client")){
                //this.switchFragment(new EndSessionPage(), false);
            }
        }
    }
    public static MainActivity getMainActivity(){
        return instance;
    }
    public Account getAccount(){return acc;}
    public Placelook getHelper(){return helper;}
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
            String st = intent.getExtras().getString(BaseCommand.Fields.message);
            Toast.makeText(getMainActivity(),st,Toast.LENGTH_LONG).show();
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
