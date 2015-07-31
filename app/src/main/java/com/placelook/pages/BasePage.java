package com.placelook.pages;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.view.View;

import com.placelook.MainActivity;

/**
 * Created by victor on 20.06.15.
 */
public class BasePage extends Fragment {
    private MainActivity act;
    protected BasePage prev;
    protected View rootView;
    protected BasePage instance;
    public BasePage(){
        super();
        act = MainActivity.getMainActivity();
        instance = this;
    }
    @SuppressLint("ValidFragment")
    public BasePage(MainActivity act){
        super();
        this.act = act;
        instance = this;
    }
    @SuppressLint("ValidFragment")
    public BasePage(MainActivity act, BasePage page){
        super();
        this.act = act;
        this.prev = page;
        instance = this;
    }
    public Context getContext(){
        return act;
    }
}
