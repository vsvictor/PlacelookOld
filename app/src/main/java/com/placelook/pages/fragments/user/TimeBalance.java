package com.placelook.pages.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.placelook.Account;
import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.commands.BaseCommand;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.utils.DateTimeOperator;

/**
 * Created by victor on 24.06.15.
 */
public class TimeBalance extends BaseFragment {
    private TextView tvTimeBalanceValue;
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }

    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.time_balance, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        Account acc = MainActivity.getMainActivity().getAccount();
        tvTimeBalanceValue = (TextView) rView.findViewById(R.id.tvTimeBalanceValue);
        tvTimeBalanceValue.setText(DateTimeOperator.secondToHHMMSS(acc.getBalance()));
    }
}