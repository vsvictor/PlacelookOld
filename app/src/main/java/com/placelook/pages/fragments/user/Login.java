package com.placelook.pages.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.pages.MainPage;
import com.placelook.pages.fragments.BaseFragment;

/**
 * Created by victor on 25.06.15.
 */
public class Login extends BaseFragment {
    private Button bLogin;
    private EditText edLogin;
    private EditText edPassword;
    private CheckBox cbSave;

    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }

    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.login, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        edLogin = (EditText) rView.findViewById(R.id.edEmail);
        edLogin.setText("dvictor74@gmail.com");
        edPassword = (EditText) rView.findViewById(R.id.edPassword);
        edPassword.setText("Vestern74");
        cbSave = (CheckBox) rView.findViewById(R.id.cbSave);
        final boolean bSave = cbSave.isChecked();
        bLogin = (Button) rView.findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sLog = edLogin.getText().toString();
                String sPas = edPassword.getText().toString();
                MainActivity.getMainActivity().getHelper().login(sLog, sPas, bSave);
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