package com.placelook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.placelook.commands.BaseCommand;
import com.placelook.pages.MainPage;
import com.placelook.pages.fragments.Claim;
import com.placelook.pages.fragments.EndSession;
import com.placelook.pages.fragments.Main;
import com.placelook.pages.fragments.OnClaim;
import com.placelook.pages.fragments.OnEndSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class MainActivity extends Activity {
    private static final String TAG = "MainAvtivity";
    private static MainActivity instance;
    private static MainPage mp;
    private EndSession es;
    private Claim claim;
    private Account acc;
    private static Placelook helper;
    private boolean active;
    private NetService netService;
    public static String last_command;
    public static Dialog waitDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View decorView = getWindow().getDecorView();
        //decorView.setBackground(getResources().getDrawable(R.drawable.rect_blue));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);
        instance = this;
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
/*
        try {
            FFmpegFrameRecorder.tryLoad();
            FFmpegFrameGrabber.tryLoad();
            Log.i(TAG, "Librari loaded");
        }
        catch (FrameGrabber.Exception ex1){}
        catch (FFmpegFrameRecorder.Exception ex2){}
*/
        helper.welcome();
        showDialog(Constants.ID_WAIT_DIALOG);
    }
    @Override
    protected Dialog onCreateDialog(int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view;
        ImageView iv = null;
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.waiting_rotate);
        if(id == Constants.ID_WAIT_DIALOG) {
            view = getLayoutInflater().inflate(R.layout.wait_dialog, null);
            builder.setView(view);
            iv = (ImageView) view.findViewById(R.id.ivWait);
            iv.startAnimation(anim);
        }
        else if(id == Constants.ID_WAIT_CANCEL){
            view = getLayoutInflater().inflate(R.layout.cancel_dialog, null);
            builder.setView(view);
            iv = (ImageView) view.findViewById(R.id.ivWaitCancel);
            RelativeLayout bCancel = (RelativeLayout) view.findViewById(R.id.bCancel);
            bCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    waitDialog.cancel();
                    MainActivity.getMainActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                }
            });
        }
        iv.startAnimation(anim);
        waitDialog = builder.create();
        return waitDialog;
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
                        replace(R.id.rlMainFragment, MainPage.getMain()).
                        add(R.id.rlFooterFragment, MainPage.getFooter()).
                        commit();
            }

            @Override
            public void onClaimToClient() {
                Bundle b = new Bundle();
                b.putString("role", "operator");
                claim.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.rlMainFragment, claim).commit();
            }

            @Override
            public void onClaimToOperator() {
                Bundle b = new Bundle();
                b.putString("role", "client");
                claim.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.rlMainFragment, claim).commit();
            }
        });

        claim.setOnClaim(new OnClaim() {
            @Override
            public void onClaim(boolean falseGoal, boolean tryOperator, boolean behaviaor, boolean req, String opt) {
                getFragmentManager().beginTransaction().
                        replace(R.id.rlMainFragment, MainPage.getMain()).
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
        startFilter.addAction("user_logout");
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
        getHelper().logout();
        Intent intent = new Intent(this, NetService.class);
        intent.putExtra("obj", "stop");
        startService(intent);
    }
    @Override
    public void onBackPressed(){
        Fragment f = getFragmentManager().findFragmentById(R.id.rlMainFragment);
        if(f instanceof EndSession) return;
        else if(f instanceof Main){
            getHelper().logout();
            finish();
            System.exit(0);
        }
        else if( f instanceof Claim){
            getFragmentManager().beginTransaction().replace(R.id.rlMainFragment, es).commit();
        }
        else super.onBackPressed();
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, null);
        String sRole = data.getExtras().getString("role");
        Bundle b = new Bundle();
        b.putString("role", sRole);
        es.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.rlMainFragment, es).commit();
    }
*/
    public static MainActivity getMainActivity(){
        return instance;
    }
    public Account getAccount(){return acc;}
    public static Placelook getHelper(){return helper;}
    private BroadcastReceiver first = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            JSONObject obj;
            try {
                obj = new JSONObject(intent.getExtras().getString("command"));
                int status = obj.getInt("status_code");
                if(status == 1 && (obj.getString("callback").equals("start"))){
                        helper.welcome();
                        //unregisterReceiver(first);
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
                    //unregisterReceiver(welc);
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
                    getAccount().setTypeAccount(false);
                    getAccount().login(false);
                    getAccount().loadTemp();
                    helper.welcome();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
