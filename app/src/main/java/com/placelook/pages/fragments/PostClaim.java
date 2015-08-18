package com.placelook.pages.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.R;

import org.w3c.dom.Text;

/**
 * Created by victor on 10.08.15.
 */
public class PostClaim extends BaseFragment {
    private TextView tvThankYou;
    private TextView tvComplaint;
    private TextView tvInCase1;
    private TextView tvInCase2;
    private TextView tvInCase3;
    private TextView tvShowAgain;
    private TextView tvMainScreen;
    private OnEndSession endSession;
    private RelativeLayout rlShowAgain;
    private RelativeLayout rlMainScreen;
    private String role;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.post_claim, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        tvThankYou = (TextView) rView.findViewById(R.id.tvThankYou);
        tvThankYou.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvComplaint = (TextView) rView.findViewById(R.id.tvComplaint);
        tvComplaint.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvInCase1 = (TextView) rView.findViewById(R.id.tvInCase1);
        tvInCase1.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvInCase2 = (TextView) rView.findViewById(R.id.tvInCase2);
        tvInCase2.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvInCase3 = (TextView) rView.findViewById(R.id.tvInCase3);
        tvInCase3.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvShowAgain = (TextView) rView.findViewById(R.id.tvShowAgain);
        tvShowAgain.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvMainScreen = (TextView) rView.findViewById(R.id.tvMainScreen);
        tvMainScreen.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        rlShowAgain = (RelativeLayout) rView.findViewById(R.id.rlShowAgain);
        rlShowAgain.setOnClickListener(new View.OnClickListener() {
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
    }
    public void setOnEndSession(OnEndSession listener){
        this.endSession = listener;
    }
}
