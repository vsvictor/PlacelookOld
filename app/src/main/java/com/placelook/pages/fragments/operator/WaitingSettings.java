package com.placelook.pages.fragments.operator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.placelook.MainActivity;
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
    private TextView tvNotificationOptions;
    private TextView tvSoundSignal;
    private TextView tvVibration;
    private TextView tvPopupWindow;

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
        tvNotificationOptions = (TextView) rView.findViewById(R.id.tvNotificationOptions);
        tvNotificationOptions.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvSoundSignal = (TextView) rView.findViewById(R.id.tvSoundSignal);
        tvSoundSignal.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvVibration = (TextView) rView.findViewById(R.id.tvVibration);
        tvVibration.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvPopupWindow = (TextView) rView.findViewById(R.id.tvPopupWindow);
        tvPopupWindow.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
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