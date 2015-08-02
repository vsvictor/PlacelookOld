package com.placelook.pages.fragments.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.R;
import com.placelook.pages.fragments.BaseFragment;
import com.placelook.video.PlayActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victor on 20.07.15.
 */
public class ViewRequest extends BaseFragment {
    private int idOper = -1;
    private EditText edTime;
    private EditText edGoal;
    private OnRequestListener listener;
    private RelativeLayout rlSend;
    private TextView tvRequestTitle;
    private TextView tvTimeInMinutes;
    private TextView tvSend;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        idOper = getArguments().getInt("operatorID");
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.view_request, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        tvRequestTitle = (TextView) rView.findViewById(R.id.tvRequestTitle);
        tvRequestTitle.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvTimeInMinutes = (TextView) rView.findViewById(R.id.tvTimeInMinutes);
        tvTimeInMinutes.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        edTime = (EditText) rView.findViewById(R.id.edMinutes);
        edTime.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        edGoal = (EditText) rView.findViewById(R.id.edGoal);
        edGoal.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvSend = (TextView) rView.findViewById(R.id.tvSend);
        tvSend.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        rlSend = (RelativeLayout) rView.findViewById(R.id.rlSend);
        rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sec = Integer.parseInt(edTime.getText().toString())*60;
                if(idOper == -1 || sec<=0 || edGoal.getText().toString().length()<=0) return;
                listener.OnRequest(idOper,sec,edGoal.getText().toString());
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter cl = new IntentFilter();
        cl.addAction("client");
        MainActivity.getMainActivity().registerReceiver(rec,cl);
    }
    public void setOnRequest(OnRequestListener listener){
        this.listener = listener;
    }
    BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getExtras().getString("command");
            try {
                JSONObject obj = new JSONObject(command);
                JSONObject param = obj.getJSONObject("param");
                String url = param.getString("url");
                int idSession = param.getInt("id");
                String role = param.getString("role");
                Intent in = new Intent(MainActivity.getMainActivity(), PlayActivity.class);
                in.putExtra("idSession", idSession);
                in.putExtra("url", url);
                in.putExtra("role", role);
                MainActivity.getMainActivity().startActivityForResult(in, 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
