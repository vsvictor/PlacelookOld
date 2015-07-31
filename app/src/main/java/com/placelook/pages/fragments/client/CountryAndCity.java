package com.placelook.pages.fragments.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.data.BaseObjectList;
import com.placelook.data.Country;
import com.placelook.data.FilteredAdapter;
import com.placelook.data.StringStringPair;
import com.placelook.pages.fragments.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by victor on 20.07.15.
 */
public class CountryAndCity extends BaseFragment {
    private AutoCompleteTextView edCountry;
    private String shortCountry;
    private AutoCompleteTextView edCity;
    private Integer idCity;
    private boolean isCitiesLoaded;
    private BaseObjectList countries;
    private BaseObjectList cities;
    private CountryAdapter adCountries;
    private CitiesAdapter adCities;
    private OnFindCity listener;
    private RelativeLayout rlFind;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        isCitiesLoaded = false;
        countries = new BaseObjectList();
        cities = new BaseObjectList();
        adCountries = null;
        adCities = null;
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.country_and_city, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        edCountry = (AutoCompleteTextView) rView.findViewById(R.id.acCountry);
        edCountry.setThreshold(1);
        edCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country pair = (Country) parent.getItemAtPosition(position);
                String countryName = pair.getName();
                edCountry.setText(countryName);
                shortCountry = pair.getKey();
                int i = 0;
            }
        });
        edCountry.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        edCity = (AutoCompleteTextView) rView.findViewById(R.id.acCity);
        edCity.setThreshold(1);
        edCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3 && !isCitiesLoaded) {
                    MainActivity.getMainActivity().getHelper().getCitiesList(shortCountry, String.valueOf(s));
                    isCitiesLoaded = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringStringPair pair = (StringStringPair) parent.getItemAtPosition(position);
                String cityName = pair.getName();
                edCity.setText(cityName);
                idCity = pair.getID();
            }
        });
        edCity.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        edCity.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        rlFind = (RelativeLayout) rView.findViewById(R.id.rlFind);
        rlFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFind(shortCountry, edCity.getText().toString());
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter ifCountries = new IntentFilter();
        ifCountries.addAction("list_countries");
        MainActivity.getMainActivity().registerReceiver(listCountries, ifCountries);
        IntentFilter ifCities = new IntentFilter();
        ifCities.addAction("list_cities");
        MainActivity.getMainActivity().registerReceiver(listCities, ifCities);
        MainActivity.getMainActivity().getHelper().getCountriesList();
    }
    @Override
    public void onPause(){
        super.onPause();
        MainActivity.getMainActivity().unregisterReceiver(listCountries);
        MainActivity.getMainActivity().unregisterReceiver(listCities);
    }
    public void setOnFindCity(OnFindCity find){
        this.listener = find;
    }
    private class CountryAdapter extends FilteredAdapter {
        public CountryAdapter(BaseObjectList list) {
            super(MainActivity.getMainActivity(), list);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = inflater.inflate(R.layout.spinner_item, null);
            Country pair = (Country) this.getData().get(position);
            TextView tvItem = (TextView) itemView.findViewById(R.id.tvSpin);
            tvItem.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));
            tvItem.setText(pair.getName());
            TextView tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            tvCount.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));
            tvCount.setText(String.valueOf(pair.getCount()));
            return itemView;
        }
    }
    private class CitiesAdapter extends FilteredAdapter{

        public CitiesAdapter(BaseObjectList list) {
            super(MainActivity.getMainActivity(), list);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = inflater.inflate(R.layout.spinner_item, null);
            StringStringPair pair = (StringStringPair) this.getData().get(position);
            TextView tvItem = (TextView) itemView.findViewById(R.id.tvSpin);
            tvItem.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Light.ttf"));
            tvItem.setText(pair.getName());
            return itemView;
        }
    }
    BroadcastReceiver listCountries = new BroadcastReceiver() {
        private JSONObject obj;
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if(countries != null)countries.clear();
                else countries = new BaseObjectList();

                obj = new JSONObject(intent.getExtras().getString("command"));

                JSONObject param = obj.getJSONObject("param");
                int count = param.getInt("list_length");
                JSONObject arr = param.getJSONObject("list");
                Iterator keys = arr.keys();
                while(keys.hasNext()){
                    String key = (String) keys.next();
                    JSONObject value = arr.getJSONObject(key);
                    String sID = key;
                    String sName = value.getString("name");
                    int iCount = value.getInt("slots_free");
                    countries.add(new Country(sID, sName, iCount));
                }
                countries.sortByName();
                edCountry.setAdapter(new CountryAdapter(countries));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    BroadcastReceiver listCities = new BroadcastReceiver() {
        private JSONObject obj;
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if(cities != null) cities.clear();
                else cities = new BaseObjectList();

                obj = new JSONObject(intent.getExtras().getString("command"));

                JSONObject param = obj.getJSONObject("param");
                int count = param.getInt("list_length");
                JSONObject arr = param.getJSONObject("list");
                Iterator keys = arr.keys();
                while(keys.hasNext()){
                    String key = (String) keys.next();
                    JSONObject o = arr.getJSONObject(key);
                    String value = o.getString("name");
                    cities.add(new StringStringPair(key, value));
                }
                cities.sortByName();
                edCity.setAdapter(new CitiesAdapter(cities));
                int i = 0;
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
