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
import de.tavendo.autobahn.WebSocketConnectionHandler;
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
                    socket.sendTextMessage(command);
                }
                @Override
                public void onTextMessage(String message) {
                    Log.i(TAG, "Message: "+message);
                    String sCallBack = null;

                    try {
                        JSONObject obj = new JSONObject(message);
                        sCallBack = obj.getString("callback");
                        if(obj.getString("cmd").equals("client_ping")){
                            JSONObject req = new JSONObject();
                            req.put("cmd","response");
                            JSONObject param = new JSONObject();
                            long l = System.currentTimeMillis();
                            param.put("time", String.valueOf(l));
                            req.put("param",param);
                            req.put("callback","ping");
                            req.put("rid", "");
                            socket.sendTextMessage(req.toString());
                            return;
                        }
                        if(obj.getString("cmd").equals("client_slot_request")){
                            obj.put("callback", "client_slot_request");
                            viewWindow(obj);
                            return;
                        }
                        else if(obj.getString("cmd").equals("client_session_start")){
                            sCallBack = obj.getJSONObject("param").getString("role");
                            int idSession = obj.getJSONObject("param").getInt("id");
                            obj.put("status_code",1);
                        }
                        else if(obj.getString("cmd").equals("client_session_action")){
                            obj.put("callback","client_session_action");
                            obj.put("status_code",1);
                            sCallBack = "client_session_action";
                        }
                        else if(obj.getString("cmd").equals("session_close")){
                            obj.remove("status_code");
                            obj.put("status_code",1);
                        }
                        else if(obj.getString("cmd").equals("client_session_close")){
                            obj.put("callback","client_session_close");
                            sCallBack = obj.getString("callback");
                            obj.put("status_code",1);
                        }

                        int resCode = obj.getInt("status_code");
                        if(resCode != 1) sCallBack = "error";
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
                    if(code != WebSocketConnectionHandler.CLOSE_NORMAL){
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("callback","error");
                            obj.put("status_code",-code);
                            Intent intent = new Intent("error");
                            intent.putExtra("command", obj.toString());
                            sendBroadcast(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
    private void send(String comm){
        if(socket != null && socket.isConnected()) socket.sendTextMessage(comm);
        else  initSocket(comm);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startID) {
        try {
            String s = intent.getExtras().getString("obj");
            if(s.equals("start")) {return Service.START_REDELIVER_INTENT;}
            else if(s.equals("stop")){
                socket.disconnect();
                stopSelf();
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
