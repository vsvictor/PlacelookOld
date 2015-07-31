package com.placelook.pages.fragments.operator;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.data.PlacelookLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by victor on 20.07.15.
 */
public class Slotter extends Service {
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction("slot_create");
        getApplicationContext().registerReceiver(receiver, filter);
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startID) {
        int accuracy = intent.getExtras().getInt("accuracy");
        criteria = new Criteria();
        criteria.setAccuracy(accuracy);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        provider = locationManager.getBestProvider(criteria, true);

        PlacelookLocation loc = getLocation();
        MainActivity.getMainActivity().getHelper().createSlot(loc);
        return Service.START_REDELIVER_INTENT;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private PlacelookLocation getLocation(){
        PlacelookLocation res = new PlacelookLocation();
        //Location location = locationManager.getLastKnownLocation(provider);
        Location location = locationManager.getLastKnownLocation("network");
        double lat = 0;
        double lng = 0;
        if(location != null){
            lat =  location.getLatitude();
            lng = location.getLongitude();
        }

        res.setLatitude(lat);
        res.setLongitude(lng);
        Geocoder gcd = new Geocoder(MainActivity.getMainActivity(), Locale.ENGLISH);
        String codeCountry = null;
        String cityName = null;
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
            if(addresses.size() <=0) throw new IOException("Index Out Of Bounds Exception");
            codeCountry = addresses.get(0).getCountryCode().toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
            codeCountry="ua";
            cityName="Kamene";
        }
        res.setCountry(codeCountry);
        res.setIDCity(-1);
        return res;
    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int res = -1;
            String s = intent.getExtras().getString("command");
            try {
                JSONObject obj = new JSONObject(s);
                res = obj.getInt("status_code");
                if(res != 1){}

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
