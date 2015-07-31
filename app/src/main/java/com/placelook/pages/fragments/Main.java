package com.placelook.pages.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.R;
import com.placelook.pages.MainPage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victor on 22.06.15.
 */
public class Main extends BaseFragment {
    private ImageView ivShow;
    private ImageView ivView;
    private TextView tvOperatorsCount;
    private TextView tvCitiesCount;
    private TextView tvCountriesCount;
    private OnClick onShow;
    private OnClick onView;
    private TextView tvPlacelook;
    private TextView tvViewTheWorld;
    private TextView tvShow;
    private TextView tvView;
    private TextView tvRight;
    private TextView tvOperators;
    private TextView tvIn;
    private TextView tvCities;
    private TextView tvCountries;
    private int operatorsCount;
    private int citiesCount;
    private int countriesCount;
    private RelativeLayout rlPress;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.main_fragment, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        tvPlacelook = (TextView) rView.findViewById(R.id.tvPlacelook);
        tvPlacelook.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Black.ttf"));
        tvViewTheWorld = (TextView) rView.findViewById(R.id.tvViewTheWirld);
        tvViewTheWorld.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvViewTheWorld.setAlpha(0.6f);

        ivShow = (ImageView) rView.findViewById(R.id.ivShow);
        ivView = (ImageView) rView.findViewById(R.id.ivView);
        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShow.onClick();
            }
        });
        ivView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onView.onClick();
            }
        });

        tvShow = (TextView) rView.findViewById(R.id.tvShow);
        tvShow.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvView = (TextView) rView.findViewById(R.id.tvView);
        tvView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));

        tvRight = (TextView) rView.findViewById(R.id.tvRight);
        tvRight.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvOperatorsCount = (TextView) rView.findViewById(R.id.tvOperatorCount);
        tvOperatorsCount.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvOperators = (TextView) rView.findViewById(R.id.tvOperators);
        tvOperators.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvIn = (TextView) rView.findViewById(R.id.tvIn);
        tvIn.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvCitiesCount = (TextView) rView.findViewById(R.id.tvCituesCount);
        tvCitiesCount.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvCities = (TextView) rView.findViewById(R.id.tvCities);
        tvCities.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvCountriesCount = (TextView) rView.findViewById(R.id.tvCountriesCount);
        tvCountriesCount.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvCountries = (TextView) rView.findViewById(R.id.tvCountries);
        tvCountries.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
    }
    @Override
    public void onResume(){
        super.onResume();
        MainPage.getHeader().setOnHeaderClickListener(new OnClick() {
            @Override
            public void onClick() {
            }
        });
        IntentFilter activeOperatorsFilter = new IntentFilter();
        activeOperatorsFilter.addAction("activity_operators");
        getContext().registerReceiver(activeOperators, activeOperatorsFilter);
        MainPage.getHeader().setTextHeader(context.getResources().getString(R.string.app_name));
        MainPage.getHeader().setInvisibleAll();
        MainPage.getHeader().setVisibleUser(true);
        setOperatorsCount(operatorsCount);
        setCitiesCount(citiesCount);
        setCountriesCount(countriesCount);
    }
    @Override
    public void onPause(){
        super.onPause();
        getContext().unregisterReceiver(activeOperators);
    }
    public void setOnShow(OnClick listener){
        this.onShow = listener;
    }
    public void  setOnView(OnClick listener){
        this.onView = listener;
    }
    public void setOperatorsCount(int count){
        String s = String.valueOf(count);
        tvOperatorsCount.setText(s);
    }
    public void setCitiesCount(int count){
        String s = String.valueOf(count);
        tvCitiesCount.setText(s);
    }
    public void setCountriesCount(int count){
        String s = String.valueOf(count);
        tvCountriesCount.setText(s);
    }
    private BroadcastReceiver activeOperators = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONObject obj = new JSONObject(intent.getExtras().getString("command"));
                JSONObject param = obj.getJSONObject("param");
                operatorsCount = param.getInt("operators");
                citiesCount = param.getInt("cities");
                countriesCount = param.getInt("countries");
                setOperatorsCount(operatorsCount);
                setCitiesCount(citiesCount);
                setCountriesCount(countriesCount);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
}
