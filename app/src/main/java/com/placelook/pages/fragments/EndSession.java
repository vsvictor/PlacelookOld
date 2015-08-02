package com.placelook.pages.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.R;

/**
 * Created by victor on 02.08.15.
 */
public class EndSession extends BaseFragment {
    private String role;
    private TextView tvTimeBalanceAdded;
    private TextView tvTimeBalance;
    private RelativeLayout rlShowAlign;
    private RelativeLayout rlMainScreen;
    private TextView tvClaimToClient;
    private TextView tvClaimToOperator;
    private OnEndSession endSession;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        try {
            role = getArguments().getString("role");
        }catch (Exception e){}
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.end_session, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        tvTimeBalanceAdded = (TextView) rView.findViewById(R.id.tvTimeBalanceAdded);
        tvTimeBalance = (TextView) rView.findViewById(R.id.tvCurrentTimeBalanceValue);
        rlShowAlign = (RelativeLayout) rView.findViewById(R.id.rlShowAgain);
        rlShowAlign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession.onShowAlign();
            }
        });
        rlMainScreen = (RelativeLayout) rView.findViewById(R.id.rlMainScreen);
        rlMainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession.onMainScreen();
            }
        });
        tvClaimToClient = (TextView) rView.findViewById(R.id.tvClaimToClient);
        tvClaimToClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession.onClaimToClient();
            }
        });
        tvClaimToOperator = (TextView) rView.findViewById(R.id.tvClaimToOperator);
        tvClaimToOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession.onClaimToOperator();
            }
        });
        if(role != null && role.equals("client")){
            tvClaimToClient.setVisibility(View.INVISIBLE);
            tvClaimToOperator.setVisibility(View.VISIBLE);
        }
    }
    public void setOnEndSession(OnEndSession listener){
        this.endSession = listener;
    }
}
