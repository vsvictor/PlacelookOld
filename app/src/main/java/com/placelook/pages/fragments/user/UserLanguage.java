package com.placelook.pages.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.placelook.R;
import com.placelook.pages.fragments.BaseFragment;

/**
 * Created by victor on 24.06.15.
 */
public class UserLanguage extends BaseFragment {
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }

    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.user_lang, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
    }
}