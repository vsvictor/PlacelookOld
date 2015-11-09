package com.placelook.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.placelook.R;
import com.placelook.controls.ExRelativeLayout;

import org.bytedeco.javacpp.avcodec;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by victor on 04.11.15.
 */
public class PlayActivity extends Activity {
    private final static String TAG = "PlayActivity";

    private int frameRate = 24;
    private int sampleAudioRateInHz = 8000;

    private String channel;
    private int idSession;
    private PlayActivity instance;
    private PowerManager.WakeLock mWakeLock;
    private ExRelativeLayout rlPlayer;
    private SurfaceView surface;
    private FFMpegPlayer player;
    //private Thread plThread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = this.getIntent();
        channel = i.getStringExtra("url");
        idSession = i.getIntExtra("idSession", -1);
        instance = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.player_surface);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        mWakeLock.acquire();

        rlPlayer = (ExRelativeLayout) findViewById(R.id.rlPlayer);
        rlPlayer.hideKeyboard();
        surface = (SurfaceView) findViewById(R.id.surface);
        player = new FFMpegPlayer(this, channel);
        player.setSampleRate(sampleAudioRateInHz);
        player.setFrameRate(frameRate);
        player.setRotate(FFMpegRotate.ROTATE_90);
        player.setSurfaceHolder(surface.getHolder());
    }
    public void onResume() {
        super.onResume();
        player.play();
    }
}