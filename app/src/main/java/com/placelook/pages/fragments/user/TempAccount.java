package com.placelook.pages.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.placelook.R;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.pages.fragments.OnClick;

/**
 * Created by victor on 24.06.15.
 */
public class TempAccount extends BaseFragment {
    private Button bRegister;
    private Button bLogin;
    private OnClick onRegister;
    private OnClick onLogin;
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }

    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.temp_account, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        bRegister = (Button) rView.findViewById(R.id.bRegister);
        bLogin = (Button) rView.findViewById(R.id.bLogin);
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister.onClick();
            }
        });
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin.onClick();
            }
        });
    }
    public void setOnRegisterListener(OnClick listener){ this.onRegister = listener;}
    public void setOnLoginListener(OnClick listener){this.onLogin = listener;}
}