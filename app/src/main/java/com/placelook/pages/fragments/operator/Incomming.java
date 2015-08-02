package com.placelook.pages.fragments.operator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    public  void setOnAccept(OnAcceptListener listener){
        this.listener = listener;
    }

}
