package com.placelook.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.*;
import android.os.Process;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.commands.Actions;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.nio.Buffer;
import java.nio.FloatBuffer;

public class PlayActivity extends Activity {
	private static PlayActivity instance;
    private String channel;// = "rtmp://192.168.1.111/myapp?/sid538";
	private int idSession;
	private PowerManager.WakeLock mWakeLock;
	private final static String CLASS_LABEL = "PlayActivity";
	private final static String LOG_TAG = CLASS_LABEL;

	private int screenWidth, screenHeight;
	private int bg_screen_bx = 232;
	private int bg_screen_by = 128;
	private int bg_screen_width = 700;
	private int bg_screen_height = 500;
	private int bg_width = 1123;
	private int bg_height = 715;

	private int live_width = 640;
	private int live_height = 480;

	private int imageWidth = 320;
	private int imageHeight = 240;
	private ImageView iv;

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
	private Animation anim;
	private boolean isGo = true;
	private Placelook helper;
    private FFmpegFrameGrabber player;
	private Frame f;
	private boolean stop = false;

	public PlayActivity(){
		helper = MainActivity.getMainActivity().getHelper();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = this.getIntent();
		channel = i.getStringExtra("url");
		//channel.replace("placelook.mobi", "192.168.1.111");
		idSession = i.getIntExtra("idSession", -1);
		instance = this;
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.player);
		iv = (ImageView) findViewById(R.id.ivFrame);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, CLASS_LABEL);
		mWakeLock.acquire();
		initPlayer();

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
					getHelper().sessionAction(idSession, Actions.GO_FORWARD);
				}
				else{
					ivClientUpYellow.startAnimation(anim);
					getHelper().sessionAction(idSession, Actions.LOOK_UP);
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
					getHelper().sessionAction(idSession, Actions.GO_BACKWARD);
				}
				else{
					ivClientDownYellow.startAnimation(anim);
					getHelper().sessionAction(idSession, Actions.LOOK_DOWN);
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
					getHelper().sessionAction(idSession, Actions.GO_LEFT);
				}
				else{
					ivClientLeftYellow.startAnimation(anim);
					getHelper().sessionAction(idSession, Actions.LOOK_LEFT);
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
					getHelper().sessionAction(idSession, Actions.GO_RIGHT);
				}
				else{
					ivClientRightYellow.startAnimation(anim);
					getHelper().sessionAction(idSession, Actions.LOOK_RIGHT);
				}
			}
		});
		rlClientStopTime = (RelativeLayout) findViewById(R.id.rlClientStopTime);
		rlClientStopTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//player.stop();
				getHelper().sessionClose(idSession);
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
				getHelper().sessionAction(idSession, Actions.STOP);
				ivClientStop.startAnimation(anim);
			}
		});
	}

	private void initPlayer() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
				Thread th = new Thread(new AudioDevice(16000,1));
				th.start();
                AndroidFrameConverter ac = new AndroidFrameConverter();
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(channel);
                try {
                    grabber.start();
                    while (true){
                        f = grabber.grab();
                        if(f == null) continue;
						if(f.samples != null) {
							//at.setData(f.samples);
						}
                        if(f.image == null) continue;
						Bitmap b1 = ac.convert(f);
						Matrix m = new Matrix();
						m.postRotate(90);
						final Bitmap b2 = Bitmap.createBitmap(b1, 0, 0, b1.getWidth(), b1.getHeight(), m, true);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								iv.setImageBitmap(b2);
							}
						});
                    }
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }catch (Exception  e){
                    e.printStackTrace();
                }
            }
        });
        th.start();
	}
	private Placelook getHelper(){
		return helper;
	}
	@Override
    public void onBackPressed() {
    }
	public class AudioDevice implements Runnable{
		AudioTrack track;
		short[] buffer = new short[1024];
		public AudioDevice(int sampleRate, int channels) {
			int minSize = AudioTrack.getMinBufferSize(sampleRate, channels == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT);
			track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
					channels == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT,
					minSize, AudioTrack.MODE_STREAM);
			track.play();
		}
		public void writeSamples(float[] samples) {
			fillBuffer(samples);
			track.write(buffer, 0, samples.length);
		}
		private void fillBuffer(float[] samples) {
			if (buffer.length < samples.length)
				buffer = new short[samples.length];
			for (int i = 0; i < samples.length; i++)
				buffer[i] = (short) (samples[i] * Short.MAX_VALUE);
		}
		public void setData(Buffer[] data){
			final java.nio.Buffer[] samples=data;//Getting the samples from the Frame from grabFrame()
			float[] smpls = null;
			if(this.track.getChannelCount()==1){//For using with mono track
				Buffer b=samples[0];
				FloatBuffer fb = (FloatBuffer)b;
				fb.rewind();
				smpls=new float[fb.capacity()];
				fb.get(smpls);
			}
			else if(this.track.getChannelCount()==2)//For using with stereo track
			{
				FloatBuffer b1=(FloatBuffer) samples[0];
				FloatBuffer b2=(FloatBuffer) samples[1];
				smpls=new float[b1.capacity()+b2.capacity()];
				for(int i=0;i<b1.capacity();i++)
				{
					smpls[2*i]=b1.get(i);
					smpls[2*i+1]=b2.get(i);
				}
			}
			this.writeSamples(smpls);
		}
		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			while (!stop){
				if(f!= null && f.samples != null) setData(f.samples);
			}
		}
	}
}
