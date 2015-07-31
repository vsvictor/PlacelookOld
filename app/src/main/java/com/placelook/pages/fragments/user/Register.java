package com.placelook.pages.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.pages.MainPage;
import com.placelook.pages.fragments.BaseFragment;

/**
 * Created by victor on 25.06.15.
 */
public class Register extends BaseFragment {
    private Button bRegistration;
    private EditText edLogin;
    private EditText edPassword;

    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }

    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.register, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        edLogin = (EditText) rView.findViewById(R.id.edEmail);
        edPassword = (EditText) rView.findViewById(R.id.edPassword);
        bRegistration = (Button) rView.findViewById(R.id.bRegistration);
        bRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sLog = edLogin.getText().toString();
                String sPas = edPassword.getText().toString();
                MainActivity.getMainActivity().getHelper().registration(sLog, sPas);
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