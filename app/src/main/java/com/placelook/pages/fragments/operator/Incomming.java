package com.placelook.pages.fragments.operator;

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
        tvGoal = (TextView) rView.findViewById(R.id.tvGoalText);
        tvTime = (TextView) rView.findViewById(R.id.tvIncomingTimeValue);
        tvGoal.setText(goal);
        String sTime = DateTimeOperator.secondToHHMMSS(time);
        tvTime.setText(sTime);
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
        //anim = AnimationUtils.loadAnimation(MainActivity.getMainActivity(), R.anim.waiting_rotate);
        //ivWaiting = (ImageView) rView.findViewById(R.id.ivWaiting);
    }
    @Override
    public void onResume(){
        super.onResume();
        //ivWaiting.startAnimation(anim);
    }
    public  void setOnAccept(OnAcceptListener listener){
        this.listener = listener;
    }

}
