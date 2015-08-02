package com.placelook.pages.fragments.operator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.R;
import com.placelook.pages.MainPage;
import com.placelook.pages.fragments.BaseFragment;

/**
 * Created by victor on 17.07.15.
 */
public class WaitingClient extends BaseFragment {
    private Animation anim;
    private ImageView ivWaiting;
    private TextView tvWaitingClient;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.waiting_client, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        anim = AnimationUtils.loadAnimation(MainActivity.getMainActivity(), R.anim.waiting_rotate);
        ivWaiting = (ImageView) rView.findViewById(R.id.ivWaiting);
        tvWaitingClient = (TextView) rView.findViewById(R.id.tvWaitingClient);
        tvWaitingClient.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
    }
    @Override
    public void onResume(){
        super.onResume();
        ivWaiting.startAnimation(anim);
    }
}
