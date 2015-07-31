package com.placelook.pages.fragments.operator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.R;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.pages.fragments.OnClick;

/**
 * Created by victor on 18.07.15.
 */
public class WaitingMinimize extends BaseFragment {
    private TextView tvTextMinimize;
    private RelativeLayout bMinimize;
    private OnClick listener;
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.waiting_minimize, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        tvTextMinimize = (TextView) rView.findViewById(R.id.tvMinimize);
        tvTextMinimize.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        bMinimize = (RelativeLayout) rView.findViewById(R.id.bMinimize);
        bMinimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
            }
        });
    }
    public void setOnMinimize(OnClick listener){
        this.listener = listener;
    }
}
