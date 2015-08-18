package com.placelook;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.placelook.commands.Ping;
import com.placelook.utils.DateTimeOperator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketMessage;

/**
 * Created by victor on 16.06.15.
 */
public class NetService extends Service {
    private WebSocketConnection socket;
    private int idCommand;
    private static final String TAG = "NetService";

    public NetService() {
        super();
        idCommand = 0;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        socket = new WebSocketConnection();
    }

    private void initSocket(final String command){
        try {
            socket.connect(Constants.server, new WebSocket.ConnectionHandler(){
                @Override
                public void onOpen() {
                    Log.i(TAG, "Connected");
                    /*Intent inStart = new Intent("start");
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("callback", "start");
                        obj.put("status_code", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    inStart.putExtra("command", obj.toString());
                    sendBroadcast(inStart);*/
                    //startPingPong(socket);
                    socket.sendTextMessage(command);
                }
                @Override
                public void onTextMessage(String message) {
                    Log.i(TAG, "Message: "+message);
                    String sCallBack = null;

                    try {
                        JSONObject obj = new JSONObject(message);
                        if(obj.getString("callback").equals("pong")) {Log.i(TAG, "Pong");return;}
                        sCallBack = obj.getString("callback");
                        if(obj.getString("cmd").equals("client_slot_request")){
                            obj.put("callback", "client_slot_request");
                            viewWindow(obj);
                            return;
                        }
                        else if(obj.getString("cmd").equals("client_session_start")){
                            sCallBack = obj.getJSONObject("param").getString("role");
                            int idSession = obj.getJSONObject("param").getInt("id");

                        }
                        else if(obj.getString("cmd").equals("client_session_action")){
                            obj.put("callback","client_session_action");
                            sCallBack = "client_session_action";
                        }
                        Intent res = new Intent(sCallBack);
                        res.putExtra("command", obj.toString());
                        sendBroadcast(res);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onRawTextMessage(byte[] payload) {}
                @Override
                public void onBinaryMessage(byte[] payload) {}
                @Override
                public void onClose(int code, String reason) {
                    Log.i(TAG, "Close");
                    stopSelf();
                    Log.i(TAG, "Stopped");
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
    private void send(String comm){
        if(socket.isConnected()) socket.sendTextMessage(comm);
        else  initSocket(comm);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startID) {
        try {
            String s = intent.getExtras().getString("obj");
            if(s.equals("start")) {return Service.START_REDELIVER_INTENT;}
            else if(s.equals("stop")){
                socket.disconnect();
                return 0;
            }
            if(s != null) {
                 send(s);
            }
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        return Service.START_REDELIVER_INTENT;
    }
    private void viewWindow(JSONObject obj){
        Intent in = new Intent(Intent.ACTION_MAIN);
        in.setComponent(new ComponentName("com.placelook", "com.placelook.MainActivity"));
        in.putExtra("command", obj.toString());
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);

        try {
            Intent insend = new Intent(obj.getString("callback"));
            insend.putExtra("command", obj.toString());
            sendBroadcast(insend);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
