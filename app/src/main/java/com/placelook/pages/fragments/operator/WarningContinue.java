package com.placelook.pages.fragments.operator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.placelook.R;
import com.placelook.commands.BaseCommand;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.pages.fragments.OnClick;

/**
 * Created by victor on 14.07.15.
 */
public class WarningContinue extends BaseFragment {
    private CheckBox cb;
    private RelativeLayout cont;
    private OnClick listener;
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.warning_continue, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        cb = (CheckBox) rView.findViewById(R.id.cbIWarninged);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cont.setEnabled(cb.isChecked());
            }
        });
        cont = (RelativeLayout) rView.findViewById(R.id.rlContinue);
        cont.setEnabled(cb.isChecked());
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
            }
        });
    }
    public void setOnContinueListener(OnClick listener){
        this.listener = listener;
    }
}
