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
import android.widget.TextView;

import com.placelook.Account;
import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.utils.DateTimeOperator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victor on 22.06.15.
 */
public class Footer extends BaseFragment {
    private Account acc;
    private TextView tvTimeBalanceValue;
    private ImageView ivAddBalance;
    private OnClick listener;
    private TextView tvTimeBalance;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        acc = MainActivity.getMainActivity().getAccount();
    }

    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.footer, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        tvTimeBalance = (TextView) rView.findViewById(R.id.tvTimeBalance);
        tvTimeBalance.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        tvTimeBalanceValue = (TextView) rView.findViewById(R.id.tvTimeBalanceValue);
        tvTimeBalanceValue.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
        ivAddBalance = (ImageView) rView.findViewById(R.id.ivAddBalance);
        ivAddBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter userDataFilter = new IntentFilter();
        userDataFilter.addAction("user_data_get");
        getContext().registerReceiver(userData, userDataFilter);
        setBalance(acc.getBalance());
    }
    @Override
    public void onPause(){
        super.onPause();
        getContext().unregisterReceiver(userData);
    }
    public void setOnAddBalanceListener(OnClick listener){
        this.listener = listener;
    }
    public void setBalance(int balance){
        String s = DateTimeOperator.secondToHHMMSS(balance);
        tvTimeBalanceValue.setText(s);
    }
    private BroadcastReceiver userData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONObject obj = new JSONObject(intent.getExtras().getString("command"));
                JSONObject param = obj.getJSONObject("param");
                //acc.setStreamingFormat(param.getInt("id_streaming_format"));
                acc.setAccountID(param.getInt("id_user"));
                acc.setBalance(param.getInt("balance"));
                acc.setEmail(param.getString("email"));
                setBalance(acc.getBalance());
                MainActivity.waitDialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
