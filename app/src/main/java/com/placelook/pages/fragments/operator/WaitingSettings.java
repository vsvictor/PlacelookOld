package com.placelook.pages.fragments.operator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.placelook.R;
import com.placelook.pages.fragments.BaseFragment;

/**
 * Created by victor on 17.07.15.
 */
public class WaitingSettings extends BaseFragment {
    private OnWaitingSettingListener listener;
    private CheckBox cbSound;
    private CheckBox cbVibrate;
    private CheckBox cbPopupWindow;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.waiting_settings, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        cbSound = (CheckBox) rView.findViewById(R.id.tgSound);
        cbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.sound(isChecked);
            }
        });
        cbVibrate = (CheckBox) rView.findViewById(R.id.tgVibration);
        cbVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.vibrate(isChecked);
            }
        });
        cbPopupWindow = (CheckBox) rView.findViewById(R.id.tgPopupWindow);
        cbPopupWindow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.popupWindow(isChecked);
            }
        });
    }
    public void setOnWaitingSetting(OnWaitingSettingListener listener){
        this.listener = listener;
    }
}