package com.placelook.video;
/*
import com.placelook.R;
import com.placelook.utils.NetworkUtils;
import com.yixia.camera.MediaRecorder;
import com.yixia.camera.MediaRecorderFilter;
import com.yixia.camera.MediaRecorder.OnErrorListener;
import com.yixia.camera.MediaRecorder.OnPreparedListener;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.camera.view.CameraNdkView;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
*/
public class RecordActivityVitamIO{// extends Activity implements OnErrorListener, OnPreparedListener{
/*    private int mWindowWidth;
    private CameraNdkView mSurfaceView;
    private MediaRecorderFilter mMediaRecorder;
    private volatile boolean mReleased;
    private Button bStart;
    private Button bStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mWindowWidth = DeviceUtils.getScreenWidth(this);
        setContentView(R.layout.recorder_io);
        mSurfaceView = (CameraNdkView) findViewById(R.id.record_preview);
        mSurfaceView.getLayoutParams().height = mWindowWidth;
        bStart = (Button) this.findViewById(R.id.bStart);
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaRecorder.startRecord();
            }
        });
        bStop = (Button) this.findViewById(R.id.bStop);
        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaRecorder.stopRecord();
            }
        });
    }
    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorderFilter();
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnPreparedListener(this);
        mMediaRecorder.setVideoBitRate(NetworkUtils.isWifiAvailable(this) ? MediaRecorder.VIDEO_BITRATE_MEDIUM : MediaRecorder.VIDEO_BITRATE_NORMAL);
        mMediaRecorder.setSurfaceView(mSurfaceView);
        String key = String.valueOf(System.currentTimeMillis());
        mMediaRecorder.prepare();
        mMediaRecorder.setCameraFilter(MediaRecorderFilter.CAMERA_FILTER_NO);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mMediaRecorder == null)
            initMediaRecorder();
        else {
            mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
            mMediaRecorder.prepare();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
        }
    }
    @Override
    public void onPrepared() {
        if (mMediaRecorder != null) {
            mMediaRecorder.autoFocus(new AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {}
                }
            });
        }
    }
    @Override
    public void onAudioError(int arg0, String arg1) {
    }
    @Override
    public void onVideoError(int arg0, int arg1) {
    }*/
}