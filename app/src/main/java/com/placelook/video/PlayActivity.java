package com.placelook.video;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

import com.placelook.Placelook;
import com.placelook.commands.Actions;
import com.placelook.MainActivity;
import com.placelook.R;

import org.json.JSONException;
import org.json.JSONObject;

//import com.placelook.service.PlacelookServiceHelper;
public class PlayActivity extends Activity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

	private static final String TAG = "RTMPPlayer";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	//private VideoView mPreview;
	private SurfaceHolder holder;
	private String path;
	private Bundle extras;
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private boolean isGo = true;
	
	private RelativeLayout rlClientLeft;
	private RelativeLayout rlClientRight;
	private RelativeLayout rlClientUp;
	private RelativeLayout rlClientDown;
	private RelativeLayout rlClientCenter;
	private RelativeLayout rlClientStop;
	private RelativeLayout rlClientStopTime;
	private ImageView ivClientLeftWhite;
	private ImageView ivClientLeftGreen;
	private ImageView ivClientLeftYellow;
	private ImageView ivClientRightWhite;
	private ImageView ivClientRightGreen;
	private ImageView ivClientRightYellow;
	private ImageView ivClientUpWhite;
	private ImageView ivClientUpGreen;
	private ImageView ivClientUpYellow;
	private ImageView ivClientDownWhite;
	private ImageView ivClientDownGreen;
	private ImageView ivClientDownYellow;
	private ImageView ivClientCenterGo;
	private ImageView ivClientCenterLook;
	private ImageView ivClientStop;
    private ImageView ivStopOperator;
    private Animation anim;
//	private PlacelookServiceHelper helper;
	private int idSession = -1;


    public PlayActivity(){

	}
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		//path = getIntent().getStringExtra("url");
		if (!LibsChecker.checkVitamioLibs(this))return;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.play);
		mPreview = (SurfaceView) findViewById(R.id.surface);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.TRANSLUCENT);
//		holder.lockCanvas().rotate(90);
//		holder.setFixedSize(mVideoWidth, mVideoHeight);

		extras = getIntent().getExtras();
		idSession = extras.getInt("idSession");
		
		ivClientCenterGo = (ImageView) findViewById(R.id.ivClientGo);
		ivClientCenterLook = (ImageView) findViewById(R.id.ivClientLook);
		ivClientCenterGo.setVisibility(View.VISIBLE);
		ivClientCenterLook.setVisibility(View.INVISIBLE);
		
		ivClientUpGreen = (ImageView) findViewById(R.id.ivClientUpGreen);
		ivClientUpYellow = (ImageView) findViewById(R.id.ivClientUpYellow);
		ivClientUpWhite = (ImageView) findViewById(R.id.ivClientUpWhite);
		ivClientUpGreen.setVisibility(View.INVISIBLE);
		ivClientUpYellow.setVisibility(View.INVISIBLE);
		ivClientUpWhite.setVisibility(View.VISIBLE);
		
		ivClientDownGreen = (ImageView) findViewById(R.id.ivClientDownGreen);
		ivClientDownYellow = (ImageView) findViewById(R.id.ivClientDownYellow);
		ivClientDownWhite = (ImageView) findViewById(R.id.ivClientDownWhite);
		ivClientDownGreen.setVisibility(View.INVISIBLE);
		ivClientDownYellow.setVisibility(View.INVISIBLE);
		ivClientDownWhite.setVisibility(View.VISIBLE);

		ivClientLeftGreen = (ImageView) findViewById(R.id.ivClientLeftGreen);
		ivClientLeftYellow = (ImageView) findViewById(R.id.ivClientLeftYellow);
		ivClientLeftWhite = (ImageView) findViewById(R.id.ivClientLeftWhite);
		ivClientLeftGreen.setVisibility(View.INVISIBLE);
		ivClientLeftYellow.setVisibility(View.INVISIBLE);
		ivClientLeftWhite.setVisibility(View.VISIBLE);

		ivClientRightGreen = (ImageView) findViewById(R.id.ivClientRightGreen);
		ivClientRightYellow = (ImageView) findViewById(R.id.ivClientRightYellow);
		ivClientRightWhite = (ImageView) findViewById(R.id.ivClientRightWhite);
		ivClientRightGreen.setVisibility(View.INVISIBLE);
		ivClientRightYellow.setVisibility(View.INVISIBLE);
		ivClientRightWhite.setVisibility(View.VISIBLE);
		
		ivClientStop = (ImageView) findViewById(R.id.ivClientStop);
		ivClientStop.setVisibility(View.VISIBLE);

        ivStopOperator = (ImageView) findViewById(R.id.ivClienStopOperator);
        ivStopOperator.setVisibility(View.INVISIBLE);
		
		anim =  AnimationUtils.loadAnimation(this, R.anim.alpha1);
		rlClientLeft = (RelativeLayout) findViewById(R.id.rlClientLeft);
		rlClientRight = (RelativeLayout) findViewById(R.id.rlClientRight);
		rlClientUp = (RelativeLayout) findViewById(R.id.rlClientUp);
		rlClientDown = (RelativeLayout) findViewById(R.id.rlClientDown);
		rlClientCenter = (RelativeLayout) findViewById(R.id.rlClientCenter);
		rlClientCenter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isGo = !isGo;
				if(isGo){
					ivClientCenterLook.setVisibility(View.INVISIBLE);
					ivClientCenterGo.setVisibility(View.VISIBLE);
				}
				else{
					ivClientCenterLook.setVisibility(View.VISIBLE);
					ivClientCenterGo.setVisibility(View.INVISIBLE);
				}
			}
		});
		rlClientUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				anim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					if(isGo){
						ivClientUpGreen.setVisibility(View.VISIBLE);
						ivClientUpYellow.setVisibility(View.INVISIBLE);
						ivClientUpWhite.setVisibility(View.INVISIBLE);
					}
					else{
						ivClientUpGreen.setVisibility(View.INVISIBLE);
						ivClientUpYellow.setVisibility(View.VISIBLE);
						ivClientUpWhite.setVisibility(View.INVISIBLE);
					}
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					ivClientUpGreen.setVisibility(View.INVISIBLE);
					ivClientUpYellow.setVisibility(View.INVISIBLE);
					ivClientUpWhite.setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}});		
				if(isGo){
					ivClientUpGreen.startAnimation(anim);
					MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.GO_FORWARD);
				}
				else{
					ivClientUpYellow.startAnimation(anim);
					MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.LOOK_UP);
				}
			}
		});
		rlClientDown.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				anim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					if(isGo){
						ivClientDownGreen.setVisibility(View.VISIBLE);
						ivClientDownYellow.setVisibility(View.INVISIBLE);
						ivClientDownWhite.setVisibility(View.INVISIBLE);
					}
					else{
						ivClientDownGreen.setVisibility(View.INVISIBLE);
						ivClientDownYellow.setVisibility(View.VISIBLE);
						ivClientDownWhite.setVisibility(View.INVISIBLE);
					}
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					ivClientDownGreen.setVisibility(View.INVISIBLE);
					ivClientDownYellow.setVisibility(View.INVISIBLE);
					ivClientDownWhite.setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}});		
				if(isGo){
					ivClientDownGreen.startAnimation(anim);
					MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.GO_BACKWARD);
				}
				else{
					ivClientDownYellow.startAnimation(anim);
					MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.LOOK_DOWN);
				}
			}
		});
		rlClientLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				anim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					if(isGo){
						ivClientLeftGreen.setVisibility(View.VISIBLE);
						ivClientLeftYellow.setVisibility(View.INVISIBLE);
						ivClientLeftWhite.setVisibility(View.INVISIBLE);
					}
					else{
						ivClientLeftGreen.setVisibility(View.INVISIBLE);
						ivClientLeftYellow.setVisibility(View.VISIBLE);
						ivClientLeftWhite.setVisibility(View.INVISIBLE);
					}
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					ivClientLeftGreen.setVisibility(View.INVISIBLE);
					ivClientLeftYellow.setVisibility(View.INVISIBLE);
					ivClientLeftWhite.setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}});		
				if(isGo){
					ivClientLeftGreen.startAnimation(anim);
					MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.GO_LEFT);
				}
				else{
					ivClientLeftYellow.startAnimation(anim);
					MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.LOOK_LEFT);
				}
			}
		});
		rlClientRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				anim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					if(isGo){
						ivClientRightGreen.setVisibility(View.VISIBLE);
						ivClientRightYellow.setVisibility(View.INVISIBLE);
						ivClientRightWhite.setVisibility(View.INVISIBLE);
					}
					else{
						ivClientRightGreen.setVisibility(View.INVISIBLE);
						ivClientRightYellow.setVisibility(View.VISIBLE);
						ivClientRightWhite.setVisibility(View.INVISIBLE);
					}
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					ivClientRightGreen.setVisibility(View.INVISIBLE);
					ivClientRightYellow.setVisibility(View.INVISIBLE);
					ivClientRightWhite.setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}});		
				if(isGo){
					ivClientRightGreen.startAnimation(anim);
					MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.GO_RIGHT);
				}
				else{
					ivClientRightYellow.startAnimation(anim);
					MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.LOOK_RIGHT);
				}
			}
		});
		rlClientStopTime = (RelativeLayout) findViewById(R.id.rlClientStopTime);
		rlClientStopTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.getMainActivity().getHelper().sessionClose(idSession);
				Intent intent = new Intent();
				intent.putExtra("role", "client");
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		rlClientStop = (RelativeLayout) findViewById(R.id.rlClientStop);
		rlClientStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.STOP);
				ivClientStop.startAnimation(anim);
			}
		});
	}
	private void playVideo(Integer Media) {
		doCleanUp();
		try {
			switch (Media) {
			case LOCAL_VIDEO:
				if (path == "") {
					Toast.makeText(PlayActivity.this, "Please edit MediaPlayerDemo_Video Activity, " + "and set the path variable to your media file path." + " Your media file must be stored on sdcard.", Toast.LENGTH_LONG).show();
					return;
				}
				break;
			case STREAM_VIDEO:
				path = extras.getString("url");
				Log.i("PlayActivity", path);
				if (path == "") {
					Toast.makeText(PlayActivity.this, "Please edit MediaPlayerDemo_Video Activity," + " and set the path variable to your media file URL.", Toast.LENGTH_LONG).show();
					return;
				}
				break;
			}
			mMediaPlayer = new MediaPlayer(this);
			mMediaPlayer.setDataSource(path);
            mMediaPlayer.setBufferSize(0);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepareAsync();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			//mMediaPlayer.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}
	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
	}
	public void onCompletion(MediaPlayer arg0) {
	}
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}
	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}
	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
	}
	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d(TAG, "surfaceDestroyed called");
	}
	public void surfaceCreated(SurfaceHolder holder) {
		//setDisplayOrientation(90);
		playVideo(this.STREAM_VIDEO);
	}
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter ifStop = new IntentFilter();
        ifStop.addAction("client_session_action");
        registerReceiver(rec, ifStop);
    }
	@Override
	protected void onPause() {
		super.onPause();
        unregisterReceiver(rec);
		releaseMediaPlayer();
		doCleanUp();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}
	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}
	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		mMediaPlayer.start();
	}

    private BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String com = intent.getExtras().getString("command");
            try {
                JSONObject obj = new JSONObject(com);
                JSONObject data = obj.getJSONObject("param");
                String command = data.getString("type");
                //log.info(com);
                blink(command);
            } catch (JSONException e) {
                e.printStackTrace();
                //log.info(e.getMessage());
            }
        }
    };
    private void blink(String act) {
        if (act.equals(Actions.STOP)) {
            ivStopOperator.setVisibility(View.VISIBLE);
        }
    }

}
