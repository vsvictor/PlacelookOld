package com.placelook.pages.fragments.operator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.R;
import com.placelook.pages.fragments.BaseFragment;

/**
 * Created by victor on 13.07.15.
 */
public class WarningText extends BaseFragment {
    private TextView tvWarning;
    private TextView tvForYourSafety;
    private TextView tvWarning11;
    private TextView tvWarning21;
    private TextView tvWarning31;
    private TextView tvWarning41;

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
        tvWarning = (TextView) rView.findViewById(R.id.tvWarning);
        tvWarning.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvForYourSafety =(TextView) rView.findViewById(R.id.tvForYourSafety);
        tvForYourSafety.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvWarning11 = (TextView) rView.findViewById(R.id.tvWarning11);
        tvWarning11.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvWarning21 = (TextView) rView.findViewById(R.id.tvWarning21);
        tvWarning21.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvWarning31 = (TextView) rView.findViewById(R.id.tvWarning31);
        tvWarning31.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvWarning41 = (TextView) rView.findViewById(R.id.tvWarning41);
        tvWarning41.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
    }
}
