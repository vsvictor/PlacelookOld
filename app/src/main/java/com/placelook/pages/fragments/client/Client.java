package com.placelook.pages.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.pages.MainPage;
import com.placelook.pages.fragments.BaseFragment;

/**
 * Created by victor on 20.07.15.
 */
public class Client extends BaseFragment {
    private CountryAndCity cc;
    private Map map;
    private ViewRequest vr;
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        cc = new CountryAndCity();
        map = new Map();
        vr = new ViewRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.client, container, false);
        return rView;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        getFragmentManager().beginTransaction().add(R.id.llClient, cc).commit();
        cc.setOnFindCity(new OnFindCity() {
            @Override
            public void onFind(String country, String city) {
                Bundle b = new Bundle();
                b.putString("country", country);
                b.putString("city", city);
                map.setArguments(b);
                getFragmentManager().beginTransaction().
                        remove(cc).
                        remove(MainPage.getFooter()).
                        add(R.id.llClient, map).
                        commit();
            }
        });
        map.setOnClickOperator(new OnClickOperator() {
            @Override
            public void onClick(int idOperator) {
                Bundle b = new Bundle();
                b.putInt("operatorID", idOperator);
                vr.setArguments(b);
                getFragmentManager().beginTransaction().
                        remove(map).
                        add(R.id.llClient, vr).
                        commit();
            }
        });
        vr.setOnRequest(new OnRequestListener() {
            @Override
            public void OnRequest(int idOper, int seconds, String goal) {
                MainActivity.getMainActivity().getHelper().slotRequest(idOper, seconds, goal);
            }
        });
    }

}