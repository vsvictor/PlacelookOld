package com.placelook.pages.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.placelook.R;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.pages.fragments.OnClick;

/**
 * Created by victor on 24.06.15.
 */
public class Signout extends BaseFragment {
    private ImageView ivSignout;
    private OnClick listener;

    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }

    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.signout, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        ivSignout = (ImageView) rView.findViewById(R.id.ivSignout);
        ivSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
            }
        });
    }
    public void setOnSignoutLisneter(OnClick listener){
        this.listener = listener;
    }
}