package com.placelook.video;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.commands.Actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avformat;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder;
import org.json.JSONException;
import org.json.JSONObject;

import com.placelook.utils.BitmapUtils;
import com.placelook.utils.VSFrameConvertor;
import com.placelook.utils.YUVPicture;

import de.mindpipe.android.logging.log4j.LogConfigurator;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class RecordActivityOld extends Activity {
	private final static String CLASS_LABEL = "RecordActivity";
	private final static String LOG_TAG = CLASS_LABEL;
	private int idSlot;
	private PowerManager.WakeLock mWakeLock;
	private long startTime = 0;
	boolean recording = false;
    private volatile FFmpegFrameRecorder recorder;
	//private volatile VSRecorder recorder;
	private boolean isPreviewOn = false;
	// private int sampleAudioRateInHz = 44100;
	private int sampleAudioRateInHz = 8000;

	// private int sendWidth = 174;
	// private int sendHeight = 144;
	// private int sendWidth = 320;
	// private int sendHeight = 240;

	private int imageWidth = 320;
	private int imageHeight = 240;
	private int format;
	private int previewWidth;
	private int previewHeight;
	//private int imageWidth = 640;
	//private int imageHeight = 480;

	private int frameRate = 24;
	//private int frameRate = 30;
	//private int bitRate = 35840;
	private int bitRate = 750000;;

	/* audio data getting thread */
	private AudioRecord audioRecord;
	private AudioRecordRunnable audioRecordRunnable;
	private Thread audioThread;
	private volatile boolean runAudioThread = true;

	/* video data getting thread */
	private Camera cameraDevice;
	private CameraView cameraView;

	private Frame yuvIplimage = null;
	private IplImage ipl;
	/* layout setting */
	private int bg_screen_bx = 232;
	private int bg_screen_by = 128;
	private int bg_screen_width = 700;
	private int bg_screen_height = 500;
	private int bg_width = 1123;
	private int bg_height = 715;

	private int live_width = 640;
	private int live_height = 480;

	private int screenWidth, screenHeight;
	private static RecordActivityOld instance;
/*
	private RelativeLayout rlStop;
	private RelativeLayout rlStopTime;
	private RelativeLayout rlView;
	private RelativeLayout rlGo;
	private RelativeLayout rlStopOperator;
	private RelativeLayout rlUpGreen;
	private RelativeLayout rlUpYellow;
	private RelativeLayout rlDownGreen;
	private RelativeLayout rlDownYellow;
	private RelativeLayout rlLeftGreen;
	private RelativeLayout rlLeftYellow;
	private RelativeLayout rlRightGreen;
	private RelativeLayout rlRightYellow;
	private TextView tvMessage;
	private Animation anim;
	private EditText edMessage;
*/
	private String channel;// = "rtmp://192.168.1.111/myapp?/sid538";
	private int idSession;
    private Logger log;
	private VSFrameConvertor converter;
	private YUVPicture picture;
	private BitmapFactory.Options opt;
	private Bitmap bmp2;
	public RecordActivityOld() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		Intent i = this.getIntent();
		channel = i.getStringExtra("url");
        log = createLogger();
		idSession = i.getIntExtra("idSession", -1);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.recorder);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, CLASS_LABEL);
		mWakeLock.acquire();
		converter = new VSFrameConvertor();//FrameConverter();
		picture = new YUVPicture();
		opt = new BitmapFactory.Options();
		opt.outWidth = 320;
		opt.outHeight = 240;

		initLayout();
		initRecorder();
/*
		edMessage = (EditText) findViewById(R.id.edMessage);
		rlGo = (RelativeLayout) findViewById(R.id.rlGo);
		rlView = (RelativeLayout) findViewById(R.id.rlView);
		rlStopOperator = (RelativeLayout) findViewById(R.id.rlStopCenter);
		rlUpGreen = (RelativeLayout) findViewById(R.id.rlToUp);
		rlUpYellow = (RelativeLayout) findViewById(R.id.rlToUpYellow);
		rlDownGreen = (RelativeLayout) findViewById(R.id.rlToDown);
		rlDownYellow = (RelativeLayout) findViewById(R.id.rlToDownYellow);
		rlLeftGreen = (RelativeLayout) findViewById(R.id.rlToLeft);
		rlLeftYellow = (RelativeLayout) findViewById(R.id.rlToLeftYellow);
		rlRightGreen = (RelativeLayout) findViewById(R.id.rlToRight);
		rlRightYellow = (RelativeLayout) findViewById(R.id.rlToRightYellow);

		rlGo.setVisibility(View.INVISIBLE);
		rlView.setVisibility(View.INVISIBLE);
		rlStopOperator.setVisibility(View.INVISIBLE);
		rlUpGreen.setVisibility(View.INVISIBLE);
		rlUpYellow.setVisibility(View.INVISIBLE);
		rlDownGreen.setVisibility(View.INVISIBLE);
		rlDownYellow.setVisibility(View.INVISIBLE);
		rlLeftGreen.setVisibility(View.INVISIBLE);
		rlLeftYellow.setVisibility(View.INVISIBLE);
		rlRightGreen.setVisibility(View.INVISIBLE);
		rlRightYellow.setVisibility(View.INVISIBLE);

		rlStop = (RelativeLayout) findViewById(R.id.rlStop);
		rlStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.getMainActivity().getHelper().sessionAction(idSession, Actions.STOP);
				rlStop.startAnimation(anim);
			}
		});

		rlStopTime = (RelativeLayout) findViewById(R.id.rlStopTime);
		rlStopTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopRecording();
				MainActivity.getMainActivity().getHelper().sessionClose(idSession);
				Intent intent = new Intent();
				intent.putExtra("role", "operator");
				intent.putExtra("idSession", idSession);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
		anim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				rlGo.setVisibility(View.INVISIBLE);
				rlView.setVisibility(View.INVISIBLE);
				rlStopOperator.setVisibility(View.INVISIBLE);
				rlUpGreen.setVisibility(View.INVISIBLE);
				rlUpYellow.setVisibility(View.INVISIBLE);
				rlDownGreen.setVisibility(View.INVISIBLE);
				rlDownYellow.setVisibility(View.INVISIBLE);
				rlLeftGreen.setVisibility(View.INVISIBLE);
				rlLeftYellow.setVisibility(View.INVISIBLE);
				rlRightGreen.setVisibility(View.INVISIBLE);
				rlRightYellow.setVisibility(View.INVISIBLE);
			}
		});
		tvMessage = (TextView) findViewById(R.id.tvMessage);
  */
        log.info("onCreate RecordActivity");
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (mWakeLock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
					CLASS_LABEL);
			mWakeLock.acquire();
		}
		//InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		//inputMethodManager.hideSoftInputFromWindow(edMessage.getWindowToken(),0);
		IntentFilter ifClient = new IntentFilter();
		ifClient.addAction("client_session_action");
		registerReceiver(client, ifClient);
		IntentFilter ifClose = new IntentFilter();
		ifClose.addAction("client_session_close");
		registerReceiver(recClose, ifClose);
        log.info("onResume RecordActivity");
	}
	@Override
	protected void onPause() {
		super.onPause();
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
		unregisterReceiver(client);
		unregisterReceiver(recClose);
        log.info("onPause RecordActivity");
	}
	@Override
	protected void onDestroy() {
		//unregisterReceiver(client);
		super.onDestroy();
		recording = false;
		if (cameraView != null) {
			cameraView.stopPreview();
			cameraView = null;
		}
		if (cameraDevice != null) {
			cameraDevice.stopPreview();
			cameraDevice.release();
			cameraDevice = null;
		}
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
        log.info("onDestroy RecordActivity");
	}
	protected void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod(
					"setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e) {
            log.info(e.getMessage());
		}
        log.info("setDisplayOrientation RecordActivity");
	}
	private void initLayout() {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		RelativeLayout.LayoutParams layoutParam = null;
		LayoutInflater myInflate = null;
		myInflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout topLayout = new RelativeLayout(this);
		FrameLayout preViewLayout = (FrameLayout) myInflate.inflate(R.layout.recorder, null);
		setContentView(preViewLayout);
		layoutParam = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
		preViewLayout.addView(topLayout, 0);
		int display_width_d = (int) (1.0 * bg_screen_width * screenWidth / bg_width);
		int display_height_d = (int) (1.0 * bg_screen_height * screenHeight / bg_height);

		int prev_rw, prev_rh;
		if (1.0 * display_width_d / display_height_d > 1.0 * live_width
				/ live_height) {
			prev_rh = display_height_d;
			prev_rw = (int) (1.0 * display_height_d * live_width / live_height);
		} else {
			prev_rw = display_width_d;
			prev_rh = (int) (1.0 * display_width_d * live_height / live_width);
		}
		// layoutParam = new RelativeLayout.LayoutParams(prev_rw, prev_rh);

		layoutParam = new RelativeLayout.LayoutParams(screenWidth, screenHeight);

		// layoutParam.topMargin = (int) (1.0 * bg_screen_by * screenHeight /
		// bg_height);
		// layoutParam.leftMargin = (int) (1.0 * bg_screen_bx * screenWidth /
		// bg_width);
		layoutParam.topMargin = 0;
		layoutParam.leftMargin = 0;

		cameraDevice = Camera.open();
		cameraDevice.setDisplayOrientation(90);
		Camera.Parameters campar = cameraDevice.getParameters();
		//List<Size> sizes = campar.getSupportedPictureSizes();
		// campar.setPictureSize(sizes.get(0).width, sizes.get(0).height);
		//format = campar.getPictureFormat();
		//previewWidth = campar.getPictureSize().width;
		//previewHeight = campar.getPreviewSize().height;

		//campar.setPictureSize(imageWidth, imageHeight);
		//log.info("Default preview picture format: "+cameraDevice.getParameters().getPreviewFormat());
		Log.i("PF", "Default preview picture format: "+cameraDevice.getParameters().getPreviewFormat());
		campar.setPictureFormat(ImageFormat.NV21);
		campar.setPreviewFormat(ImageFormat.NV21);
		try {
			cameraDevice.setParameters(campar);
		}
		catch (Exception ex){
			log.info("Preview picture format: "+cameraDevice.getParameters().getPreviewFormat());
		}
		cameraView = new CameraView(this, cameraDevice);
		topLayout.addView(cameraView, layoutParam);
        log.info("initLayout RecordActivity");
	}
	private void initRecorder() {
		if (yuvIplimage == null) {
			yuvIplimage = new Frame(imageWidth, imageHeight,Frame.DEPTH_UBYTE, 2);
		}

		recorder = new FFmpegFrameRecorder(channel, imageWidth, imageHeight,1);
		recorder.setFormat("flv");
		recorder.setSampleRate(sampleAudioRateInHz);
		recorder.setFrameRate(frameRate);
		recorder.setVideoQuality(25);
		recorder.setInterleaved(true);
		recorder.setVideoBitrate(bitRate);
		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
		recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);

		recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
		recorder.setVideoOption("preset", "ultrafast");
		recorder.setVideoOption("tune", "zerolatency");
		recorder.setVideoOption("cpuflags", "vfpv3");
		recorder.setVideoOption("cpuflags", "neon");
		recorder.setVideoOption("fflags", "nobuffer");
		recorder.setVideoOption("rtbufsize", "0");
		recorder.setVideoOption("analyzeduration", "0");
		recorder.setVideoOption("probesize", "32");

		recorder.setGopSize(120);

		//recorder.setVideoOption("max_picture_buffer", "51200");
		//recorder.setVideoOption("probesize", "192");
		//recorder.setVideoOption("max_index_size", "51200");
		audioRecordRunnable = new AudioRecordRunnable();
		audioThread = new Thread(audioRecordRunnable);
		runAudioThread = true;

		log.info("initRecorder RecordActivity");
	}
	public void startRecording() {

		try {
			recorder.start();
			startTime = System.currentTimeMillis();
			recording = true;
			audioThread.start();

		} catch (FFmpegFrameRecorder.Exception e) {
			e.printStackTrace();
            log.info(e.getMessage());

		}
        log.info("starrRecording RecordActivity");
	}
	public void stopRecording() {
		runAudioThread = false;
		try {
			audioThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
            log.info(e.getMessage());
		}
		audioRecordRunnable = null;
		audioThread = null;
		if (recorder != null && recording) {
			recording = false;
			try {
				recorder.stop();
				recorder.release();
			} catch (FFmpegFrameRecorder.Exception e) {
				e.printStackTrace();
                log.info(e.getMessage());
			}
			recorder = null;
		}
        log.info("stopRecording RecordActivity");
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (recording) {
				stopRecording();
			}
            log.info("onKeyDown finish RecordActivity");
			finish();
			return true;
		}
        log.info("onKeyDown RecordActivity");
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
        log.info("onConfigurationChanged RecordActivity");
	}
	class AudioRecordRunnable implements Runnable {
		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			int bufferSize;
			short[] audioData;
			int bufferReadResult;
			bufferSize = AudioRecord
					.getMinBufferSize(sampleAudioRateInHz,
							AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT);
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.CAMCORDER,
					sampleAudioRateInHz, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize);
/*			boolean isAvailable = AcousticEchoCanceler.isAvailable();
			if (isAvailable) {
				AcousticEchoCanceler aec = AcousticEchoCanceler.create(audioRecord.getAudioSessionId());
				aec.setEnabled(true);
			}*/
			audioData = new short[bufferSize];
			audioRecord.startRecording();
            log.info("startAudio RecordActivity");
			while (runAudioThread) {
				bufferReadResult = audioRecord.read(audioData, 0,
						audioData.length);
				if (bufferReadResult > 0) {
					if (recording) {
						try {
							recorder.recordSamples(ShortBuffer.wrap(audioData,
									0, bufferReadResult));
						} catch (FFmpegFrameRecorder.Exception e) {
							e.printStackTrace();
                            log.info(e.getMessage());
						}
					}
				}
			}
            log.info("end audio cycle RecordActivity");
			if (audioRecord != null) {
				audioRecord.stop();
				audioRecord.release();
				audioRecord = null;
			}
		}
	}
	class CameraView extends SurfaceView implements SurfaceHolder.Callback,	PreviewCallback {
		private SurfaceHolder mHolder;
		private Camera mCamera;
		//private ArrayDeque<IplImage> q;

		public CameraView(Context context, Camera camera) {
			super(context);
			mCamera = camera;
			mHolder = getHolder();
			mHolder.addCallback(CameraView.this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			mCamera.setPreviewCallback(CameraView.this);
			//q = new ArrayDeque<IplImage>();
            log.info("CameraView constructor RecordActivity");
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				stopPreview();
				setDisplayOrientation(mCamera, 90);
				mCamera.setPreviewDisplay(holder);
				startRecording();
			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
                log.info(exception.getMessage());
			}
            log.info("surfaceCreated RecordActivity");
		}
		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
			//Camera.Parameters camParams = mCamera.getParameters();
			//camParams.setPreviewSize(imageWidth, imageHeight);
			//previewWidth = imageWidth;
			//previewHeight = imageHeight;
			//camParams.setPreviewFrameRate(frameRate);
			//mCamera.setParameters(camParams);
			startPreview();
            log.info("surfaceChanged RecordActivity");
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			try {
				mHolder.addCallback(null);
				mCamera.setPreviewCallback(null);
			} catch (RuntimeException e) {
                log.info(e.getMessage());
			}
            log.info("surfaceDestroyed RecordActivity");
		}

		public void startPreview() {
			if (!isPreviewOn && mCamera != null) {
				isPreviewOn = true;
				mCamera.startPreview();
			}
		}
		public void stopPreview() {
			if (isPreviewOn && mCamera != null) {
				isPreviewOn = false;
				mCamera.stopPreview();
			}
            log.info("stopPreview RecordActivity");
		}
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			int w = camera.getParameters().getPictureSize().width;
			int h = camera.getParameters().getPictureSize().height;
			YuvImage im = new YuvImage(data,ImageFormat.NV21,320,240,null);
			Log.i("Record","W:H "+im.getWidth()+":"+im.getHeight());
			//Bitmap bmp = BitmapUtils.createBitmapFromYuv(data,w, h);
			//Log.i("Record","Size W:H "+bmp.getWidth()+":"+bmp.getHeight());
			//bmp2 = BitmapUtils.createBitmapFromYuv(data,320,240);
			//Log.i("Record","Size W:H "+bmp2.getWidth()+":"+bmp2.getHeight());
			if (yuvIplimage != null && recording) {
				((ByteBuffer) yuvIplimage.image[0].position(0)).put(im.getYuvData());
				//yuvIplimage = converter.convert(picture.convert(data,imageWidth,imageHeight,1), 320, 240, 3);
				//yuvIplimage = converter.convert(bmp2);
                long t = 1000 * (System.currentTimeMillis() - startTime);
				if (t > recorder.getTimestamp()) {
					recorder.setTimestamp(t);
				}
                try {
                    recorder.record(yuvIplimage);
					//recorder.record(f);
                } catch (FrameRecorder.Exception e) {
					Log.i("Record", e.getMessage());
                }
			}
		}
	}
	BroadcastReceiver client = new BroadcastReceiver() {
	@Override
	public void onReceive(Context context, Intent intent) {
		String message = "";
		String com = intent.getExtras().getString("command");
		try {
			JSONObject obj = new JSONObject(com);
			JSONObject data = obj.getJSONObject("param");
			String command = data.getString("type");
            log.info(com);
			if(command.equals(Actions.TEXT)){
				message = data.getString("msg");
			}
			//blink(command, message);
		} catch (JSONException e) {
			e.printStackTrace();
            log.info(e.getMessage());
		}
	}
	};
/*
	private void blink(String act, String message) {
		if (act.equals(Actions.GO_FORWARD)) {
			rlGo.setVisibility(View.VISIBLE);
			rlUpGreen.setVisibility(View.VISIBLE);
			rlUpGreen.startAnimation(anim);
		} else if (act.equals(Actions.GO_BACKWARD)) {
			rlGo.setVisibility(View.VISIBLE);
			rlDownGreen.setVisibility(View.VISIBLE);
			rlDownGreen.startAnimation(anim);
		} else if (act.equals(Actions.GO_LEFT)) {
			rlGo.setVisibility(View.VISIBLE);
			rlLeftGreen.setVisibility(View.VISIBLE);
			rlLeftGreen.startAnimation(anim);
		} else if (act.equals(Actions.GO_RIGHT)) {
			rlGo.setVisibility(View.VISIBLE);
			rlRightGreen.setVisibility(View.VISIBLE);
			rlRightGreen.startAnimation(anim);
		} else if (act.equals(Actions.LOOK_UP)) {
			rlView.setVisibility(View.VISIBLE);
			rlUpYellow.setVisibility(View.VISIBLE);
			rlUpYellow.startAnimation(anim);
		} else if (act.equals(Actions.LOOK_DOWN)) {
			rlView.setVisibility(View.VISIBLE);
			rlDownYellow.setVisibility(View.VISIBLE);
			rlDownYellow.startAnimation(anim);
		} else if (act.equals(Actions.LOOK_LEFT)) {
			rlView.setVisibility(View.VISIBLE);
			rlLeftYellow.setVisibility(View.VISIBLE);
			rlLeftYellow.startAnimation(anim);
		} else if (act.equals(Actions.LOOK_RIGHT)) {
			rlView.setVisibility(View.VISIBLE);
			rlRightYellow.setVisibility(View.VISIBLE);
			rlRightYellow.startAnimation(anim);
		} else if (act.equals(Actions.STOP)) {
			rlStopOperator.setVisibility(View.VISIBLE);
			rlStopOperator.startAnimation(anim);
		} else if(act.equals(Actions.TEXT)){
			tvMessage.setText(message);
		}

	}
*/
	BroadcastReceiver recClose = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			stopRecording();
			//MainActivity.getMainActivity().getHelper().sessionClose(idSession);
			Intent in = new Intent();
			in.putExtra("role", "operator");
			in.putExtra("idSession", idSession);
			instance.setResult(RESULT_OK, in);
			instance.finish();
		}
	};
	@Override
	public void onBackPressed() {
	}

    private Logger createLogger(){
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                + File.separator + "placelook.txt");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ALL);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
        Logger logger = Logger.getLogger(String.valueOf(RecordActivity.class));
        return logger;
    }
}
