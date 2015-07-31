package com.placelook.pages.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.placelook.Account;
import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.pages.MainPage;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.pages.fragments.user.Login;
import com.placelook.pages.fragments.user.PermanentAccount;
import com.placelook.pages.fragments.user.Register;
import com.placelook.pages.fragments.user.Signout;
import com.placelook.pages.fragments.user.TempAccount;
import com.placelook.pages.fragments.user.TimeBalance;
import com.placelook.pages.fragments.user.UserLanguage;
import com.placelook.pages.fragments.user.UserStats;

/**
 * Created by victor on 24.06.15.
 */
public class User extends BaseFragment {
    private Account acc;
    private TempAccount tacc;
    private PermanentAccount pacc;
    private TimeBalance tb;
    private UserLanguage ul;
    private UserStats us;
    private Signout so;
    private static User instance;
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        instance = this;
        tacc = new TempAccount();
        pacc = new PermanentAccount();
        tb = new TimeBalance();
        ul = new UserLanguage();
        us = new UserStats();
        so = new Signout();
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.user_account, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        acc = MainActivity.getMainActivity().getAccount();
        final BaseFragment f = acc.isTemporary()?tacc:pacc;
        getFragmentManager().beginTransaction().
                add(R.id.llUserAccount, f).
                add(R.id.llUserAccount, tb).
                add(R.id.llUserAccount, ul).
                add(R.id.llUserAccount, us).
                commit();
        if(acc.isTemporary()) getFragmentManager().beginTransaction().remove(MainPage.getFooter()).commit();
        else getFragmentManager().beginTransaction().replace(R.id.rlFooterFragment, so).commit();
        MainPage.getHeader().setVisibleUser(false);
        MainPage.getHeader().setVisiblePrev(true);
        MainPage.getHeader().setTextHeader(context.getResources().getString(R.string.my_account));

        if(tacc != null){
            tacc.setOnRegisterListener(new OnClick() {
                @Override
                public void onClick() {
                    Register reg = new Register();
                    getFragmentManager().beginTransaction().
                            remove(MainPage.getHeader()).
                            remove(f).
                            remove(tb).
                            remove(ul).
                            remove(us).
                            add(R.id.llUserAccount, reg).
                            commit();
                }
            });
            tacc.setOnLoginListener(new OnClick() {
                @Override
                public void onClick() {
                    Login log = new Login();
                    getFragmentManager().beginTransaction().
                            remove(MainPage.getHeader()).
                            remove(f).
                            remove(tb).
                            remove(ul).
                            remove(us).
                            add(R.id.llUserAccount, log).
                            commit();
                }
            });
            so.setOnSignoutLisneter(new OnClick() {
                @Override
                public void onClick() {
                    MainActivity.getMainActivity().getHelper().logout();
                    if(!MainActivity.getMainActivity().getAccount().isTemporary()) {
                        getFragmentManager().beginTransaction().remove(so).commit();
                    }
                    getFragmentManager().beginTransaction().
                            remove(getCurrent()).
                            add(R.id.rlHeaderFragment, MainPage.getHeader()).
                            add(R.id.rlMainFragment, MainPage.getMain()).
                            add(R.id.rlFooterFragment, MainPage.getFooter()).
                            commit();
                }
            });
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        MainPage.getHeader().setVisibleUser(false);
        MainPage.getHeader().setVisiblePrev(true);
        MainPage.getHeader().setTextHeader(context.getResources().getString(R.string.my_account));
        MainPage.getHeader().setOnHeaderClickListener(new OnClick() {
            @Override
            public void onClick() {
                if (acc.isTemporary())
                    getFragmentManager().beginTransaction().add(R.id.rlFooterFragment, MainPage.getFooter()).commit();
                else
                    getFragmentManager().beginTransaction().replace(R.id.rlFooterFragment, MainPage.getFooter()).commit();
                MainActivity.getMainActivity().onBackPressed();
            }
        });
    }
}
