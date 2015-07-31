package com.placelook.pages.fragments.operator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.placelook.R;
import com.placelook.pages.fragments.BaseFragment;

/**
 * Created by victor on 13.07.15.
 */
public class WarningText extends BaseFragment {
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.warning_text, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
    }
}
