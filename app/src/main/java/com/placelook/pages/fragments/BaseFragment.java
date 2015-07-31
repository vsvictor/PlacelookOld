package com.placelook.pages.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.view.View;

import com.placelook.MainActivity;
import com.placelook.pages.BasePage;

/**
 * Created by victor on 21.06.15.
 */
public class BaseFragment extends Fragment {
    private BasePage page;
    protected View rView;
    protected Context context;
    public BaseFragment(){
        super();
        this.page = null;
        this.context = MainActivity.getMainActivity();
    }
    @SuppressLint("ValidFragment")
    public BaseFragment(BasePage page){
        super();
        this.page = page;
        this.context = MainActivity.getMainActivity();
    }
    protected Context getContext(){
        return context;
    }
    public Fragment getCurrent(){
        return this;
    }
}
