package com.placelook.video;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
//import android.media.AudioFormat;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.commands.Actions;
import com.placelook.controls.ExRelativeLayout;
import com.placelook.data.SyncQueue;
import com.placelook.utils.DateTimeOperator;
import com.placelook.utils.Rotator;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivityOld extends Activity {
    private static PlayActivityOld instance;
    private String channel;// = "rtmp://192.168.1.111/myapp?/sid538";
    private int idSession;
    private PowerManager.WakeLock mWakeLock;
    private final static String CLASS_LABEL = "PlayActivity";
    private final static String LOG_TAG = CLASS_LABEL;

    private int frameRate = 24;
    private int bitRate = 35840;
    private int sampleAudioRateInHz = 8000;

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

    private ExRelativeLayout rlPlayer;
    private RelativeLayout rlClientLeft;
    private RelativeLayout rlClientRight;
    private RelativeLayout rlClientUp;
    private RelativeLayout rlClientDown;
    private RelativeLayout rlClientCenter;
    private RelativeLayout rlClientStop;
    private RelativeLayout rlClientStopTime;
    private RelativeLayout rlStopOperator;
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
    private EditText edMessage;
    private TextView tvMessage;
    private ImageView ivMessage;
    private RelativeLayout rlMessage;
    private TextView tvClientStopTime;

    private boolean isGo = true;
    private Placelook helper;
    private FFmpegFrameGrabber player;
    private volatile Frame f;
    private boolean stop = false;
    //private AudioDevice at;
    //private volatile Buffer[] mainBuffer;
    private Animation clientAnim;
    private Thread playerth;
    //private volatile SyncQueue q;
    private Bitmap b1;
    private Bitmap b2;

    public PlayActivityOld() {
        helper = MainActivity.getMainActivity().getHelper();
        //q= new SyncQueue();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = this.getIntent();
        channel = i.getStringExtra("url");
        idSession = i.getIntExtra("idSession", -1);
        instance = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.player);
        iv = (ImageView) findViewById(R.id.ivFrame);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, CLASS_LABEL);
        mWakeLock.acquire();


        rlPlayer = (ExRelativeLayout) findViewById(R.id.rlPlayer);
/*
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
		
		anim = AnimationUtils.loadAnimation(this, R.anim.alpha1);
		clientAnim = AnimationUtils.loadAnimation(this, R.anim.alpha1);
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
                instance.stop = true;
                getHelper().sessionClose(idSession);
				Intent intent = new Intent();
				intent.putExtra("role", "client");
				intent.putExtra("idSession", idSession);
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

		rlStopOperator = (RelativeLayout) findViewById(R.id.rlStopCenter);
		edMessage = (EditText) findViewById(R.id.edMessage);
		rlMessage = (RelativeLayout) findViewById(R.id.rlMessage);

		tvMessage = (TextView) findViewById(R.id.tvMessage);
		edMessage.setVisibility(View.INVISIBLE);
		tvMessage.setVisibility(View.VISIBLE);
		rlMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				edMessage.setVisibility(View.VISIBLE);
				tvMessage.setVisibility(View.INVISIBLE);
				edMessage.requestFocus();
				rlPlayer.showKeyboard();
			}
		});
		ivMessage = (ImageView) findViewById(R.id.ivMessagePicture);
		ivMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String sText = edMessage.getText().toString();
				MainActivity.getHelper().sessionAction(idSession, sText);
				edMessage.setText("");
				edMessage.setVisibility(View.INVISIBLE);
				tvMessage.setVisibility(View.VISIBLE);
				rlStopOperator.requestFocus();
				rlPlayer.hideKeyboard();
			}
		});
		tvClientStopTime = (TextView) findViewById(R.id.tvClientStopTime);
*/
        initPlayer();
        //at = new AudioDevice(8000,1);
        //final Thread th = new Thread(at);
        //th.start();
		/*q.setOnAdded(new SyncQueueOnAdded() {
			@Override
			public void onAdded() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						do {
							Bitmap bb = (Bitmap) q.pop();
							iv.setImageBitmap(bb);
							//iv.invalidate();
						}while (!q.isEmpty());
					}
				});
				Log.i(LOG_TAG, "Size: " + q.size());
			}
		});*/
    }

    private void initPlayer() {
        playerth = new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DISPLAY);
                AndroidFrameConverter ac = new AndroidFrameConverter();
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(channel);
                //grabber.setVideoBitrate(bitRate);// 35 kbps
                grabber.setFormat("flv");
                grabber.setSampleRate(sampleAudioRateInHz);
                grabber.setFrameRate(frameRate);
                grabber.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                grabber.setAudioCodec(avcodec.AV_CODEC_ID_AAC);

                grabber.setVideoOption("tune", "zerolatency");
                grabber.setVideoOption("cpuflags", "vfpv3");
                grabber.setVideoOption("cpuflags", "neon");
                grabber.setVideoOption("probesize", "32");
                grabber.setVideoOption("fflags", "nobuffer");
                grabber.setVideoOption("analyzeduration", "0");
                grabber.setVideoOption("rtbufsize", "0");

				//AudioFormat audioFormat = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true, true);

				//AndroidAudioDevice dev = new AndroidAudioDevice(sampleAudioRateInHz,1);

				try {
                    grabber.start();
                    while (!instance.stop) {
                        f = grabber.grab();
                        if (f == null) continue;
                        //if(f.samples != null) dev.fillBuffer(f.samples);
                        if (f.image != null) { //continue;
                            b1 = ac.convert(f);
                            Matrix m = new Matrix();
                            m.postRotate(90);
                            b2 = Bitmap.createBitmap(b1, 0, 0, b1.getWidth(), b1.getHeight(), m, true);
                            //q.push(b1);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setImageBitmap(b2);
                                    Log.i("UI", "Drawed frame");

                                }
                            });
                        }
                        Log.i("GB", "Grabbed frame");
                    }
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        playerth.start();
    }

    private Placelook getHelper() {
        return helper;
    }
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter ifClient = new IntentFilter();
        ifClient.addAction("client_session_action");
        registerReceiver(oper, ifClient);

        IntentFilter ifClose = new IntentFilter();
        ifClose.addAction("client_session_close");
        registerReceiver(recClose, ifClose);
        final long begTime = System.currentTimeMillis();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final Long l = System.currentTimeMillis() - begTime;
                final int ii = l.intValue() / 1000;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //tvClientStopTime.setText(DateTimeOperator.secondToHHMMSS(ii));
                    }
                });

            }
        }, 0, 1000);
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(recClose);
        unregisterReceiver(oper);
        this.stop = true;
    }
    @Override
    public void onBackPressed() {
    }
    BroadcastReceiver oper = new BroadcastReceiver() {
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
    BroadcastReceiver recClose = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            instance.stop = true;
            getHelper().sessionClose(idSession);
            Intent in = new Intent();
            in.putExtra("role", "client");
            in.putExtra("idSession", idSession);
            instance.setResult(RESULT_OK, in);
            instance.finish();
        }
    };

    private void blink(String act) {
        if (act.equals(Actions.STOP)) {
            rlStopOperator.setVisibility(View.VISIBLE);
            rlStopOperator.startAnimation(clientAnim);
            clientAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rlStopOperator.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    /*
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
                if( data == null || data.length<1) return;
                if(this.track.getChannelCount()==1){//For using with mono track
                    if(samples.length<1 || samples[0] == null) return;
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
                    if(samples.length<2 || samples[0]==null || samples[1]==null) return;
                    smpls=new float[b1.capacity()+b2.capacity()];
                    for(int i=0;i<b1.capacity();i++)
                    {
                        smpls[2*i]=b1.get(i);
                        smpls[2*i+1]=b2.get(i);
                    }
                }
                this.writeSamples(smpls);
                buffer = new short[1024];
            }
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                while (!stop){
                    if(f!= null && f.samples != null) setData(mainBuffer);
                    //mainBuffer = null;
                }
            }
        }
    */
}
