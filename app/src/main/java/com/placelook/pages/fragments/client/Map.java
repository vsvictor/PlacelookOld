package com.placelook.pages.fragments.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.data.BaseObjectList;
import com.placelook.data.Operator;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.pages.fragments.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by victor on 20.07.15.
 */
public class Map extends BaseFragment {
    private ExMapView mv;
    private String country;
    private String city;
    private BaseObjectList operators;
    private Operator selectedOperator;
    private OnClickOperator listener;
    private boolean all;
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        try {
            country = getArguments().getString("country");
            city = getArguments().getString("city");
            all = false;
        } catch (Exception e){
            all = true;
        }

    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.map, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        MapsInitializer.initialize(MainActivity.getMainActivity());
        mv = (ExMapView) rView.findViewById(R.id.exmapview);
        mv.onCreate(saved);
        mv.onResume();

        if(Geocoder.isPresent()){
            CameraUpdate upd = null;
            try {
                Geocoder gc = new Geocoder(MainActivity.getMainActivity());
                List<Address> addresses= gc.getFromLocationName(city, 1);
                Address ad = addresses.get(0);
                upd = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().
                        target(new LatLng(ad.getLatitude(),ad.getLongitude())).zoom(9).build());

            } catch (IOException e) {
                upd = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().
                        target(new LatLng(0,0)).zoom(5).build());
            } catch (IllegalArgumentException e){
                //upd = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().
                //        target(new LatLng(0,0)).zoom(5).build());
            }
            if(upd != null)  mv.getMap().animateCamera(upd);
        }
        mv.getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker aMarker) {
                int idOperator = -1;
                for(int i = 0; i<operators.size();i++){
                    Marker m = ((Operator)operators.get(i)).getMarker();
                    if(m.equals(aMarker)){
                        selectedOperator = (Operator) operators.get(i);
                        listener.onClick(selectedOperator.getID());
                        break;
                    }
                }

                return false;
            }
        });
        if(all){
            MainActivity.getMainActivity().getHelper().slotSearch();
        }else {
            MainActivity.getMainActivity().getHelper().slotSearch(country);
        }

    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter opFilter = new IntentFilter();
        opFilter.addAction("slot_search");
        MainActivity.getMainActivity().registerReceiver(listOperators, opFilter);
    }
    @Override
    public void onPause(){
        super.onPause();;
        MainActivity.getMainActivity().unregisterReceiver(listOperators);
    }
    public void setOnClickOperator(OnClickOperator listener){
        this.listener = listener;
    }
    private void drawMarkers(){
        for(int i = 0; i< operators.size(); i++){
            Operator op = (Operator) operators.get(i);
            op.setMarker(mv.getMap().addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(new LatLng(op.getWidth(), op.getHeigth()))));
        }
    }

    BroadcastReceiver listOperators = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(operators != null) operators.clear();
            else operators = new BaseObjectList();


            JSONObject obj = null;
            try {
                obj = new JSONObject(intent.getExtras().getString("command"));
                JSONObject param = obj.getJSONObject("param");
                int count = param.getInt("list_length");
                JSONObject arr = param.getJSONObject("list");
                Iterator keys = arr.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    JSONObject value = arr.getJSONObject(key);
                    String country = value.getString("country");
                    int idCity = value.getInt("id_city");
                    double width = value.getDouble("width");
                    double height = value.getDouble("height");
                    //boolean isGPS = value.getInt("gps") == 1 ? true : false;
                    boolean isGPS = false;
                    Operator oper = new Operator(Integer.parseInt(key), key, country, idCity, width, height, isGPS);
                    operators.add(oper);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            drawMarkers();
        }
    };
}
