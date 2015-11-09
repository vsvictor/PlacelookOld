package com.placelook.pages.fragments.operator;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.Constants;
import com.placelook.MainActivity;
import com.placelook.R;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.utils.DateTimeOperator;

/**
 * Created by victor on 20.07.15.
 */
public class Incomming extends BaseFragment {
    private Animation anim;
    private ImageView ivWaiting;
    private int slot;
    private int time;
    private String goal;
    private TextView tvGoal;
    private TextView tvTime;
    private OnAcceptListener listener;
    private RelativeLayout rlAccept;
    private RelativeLayout rlReject;
    private TextView tvIncommingRequest;
    private TextView tvGoalText;
    private TextView tvIncommingTime;
    private TextView tvAccept;
    private TextView tvReject;
    private boolean stop = false;
    private Thread thSount;
    private Thread thVibrate;
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        slot = getArguments().getInt("slot");
        time = getArguments().getInt("time");
        goal = getArguments().getString("goal");
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.incomming, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        tvIncommingRequest = (TextView) rView.findViewById(R.id.tvIncommingRequest);
        tvIncommingRequest.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvGoal = (TextView) rView.findViewById(R.id.tvGoal);
        tvGoal.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvGoalText = (TextView) rView.findViewById(R.id.tvGoalText);
        tvGoalText.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Bold.ttf"));
        tvIncommingTime = (TextView) rView.findViewById(R.id.tvIncomingTime);
        tvIncommingTime.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvTime = (TextView) rView.findViewById(R.id.tvIncomingTimeValue);
        tvTime.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Bold.ttf"));
        tvGoal.setText(goal);
        String sTime = DateTimeOperator.secondToHHMMSS(time);
        tvTime.setText(sTime);
        tvAccept = (TextView) rView.findViewById(R.id.tvAccept);
        tvAccept.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvReject = (TextView) rView.findViewById(R.id.tvReject);
        tvReject.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        rlAccept = (RelativeLayout) rView.findViewById(R.id.rlAccept);
        rlReject = (RelativeLayout) rView.findViewById(R.id.rlReject);
        rlAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAccept(slot);
            }
        });
        rlReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReject(slot);
            }
        });
        stop = false;
        if(getSound()){
            thSount = new Thread(new Runnable() {
                @Override
                public void run() {
                    Uri notification = Uri.parse("android.resource://com.placelook/"+R.raw.europa);//RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    Ringtone r = RingtoneManager.getRingtone(MainActivity.getMainActivity(), notification);
                    r.play();
                }
            });
            thSount.start();
        }
        if(getVibrate()){
            thVibrate = new Thread(new Runnable() {
                @Override
                public void run() {
                    vibrateSOS();
                }
            });
            thVibrate.start();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
        try {
            this.stop = true;
            thSount.join();
            thVibrate.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public  void setOnAccept(OnAcceptListener listener){
        this.listener = listener;
    }
    private boolean getSound(){
        SharedPreferences sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
        boolean res = sh.getBoolean("sound", true);
        return res;
    }
    private  boolean getVibrate(){
        SharedPreferences sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
        boolean res = sh.getBoolean("vibrate", true);
        return res;
    }
    private void vibrateSOS(){
        Vibrator v = (Vibrator) this.context.getSystemService(MainActivity.getMainActivity().VIBRATOR_SERVICE);
        try {
            v.vibrate(1500);
            Thread.sleep(1000);
            v.vibrate(1500);
            Thread.sleep(1000);
            v.vibrate(1500);
            Thread.sleep(1000);
            v.vibrate(1000);
            Thread.sleep(500);
            v.vibrate(1000);
            Thread.sleep(500);
            v.vibrate(1000);
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }
    public void stop(){
        stop = true;
    }
}
