package com.placelook.video;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.SurfaceView;
import android.widget.ImageView;

import de.mindpipe.android.logging.log4j.LogConfigurator;


public class VSPlayer extends FFmpegFrameGrabber{
//	private int sampleAudioRateInHz = 44100;
	private int sampleAudioRateInHz = 16000;	
	private int frameRate = 24;

	private ImageView viewer;
	private boolean stop = false;
	private Activity context;
	private long count = 0;
	private Logger logger;
	public VSPlayer(Activity context, String stream) {
		super(stream);
		//this.init();
		this.setFormat("flv");
		this.setSampleRate(sampleAudioRateInHz);
		this.setFrameRate(frameRate);
		this.setVideoCodec(avcodec.AV_CODEC_ID_H264);
		this.setAudioCodec(avcodec.AV_CODEC_ID_AAC);

		this.setVideoOption("crf", "28");
		this.setVideoOption("preset", "slow");
		this.setVideoOption("tune", "zerolatency");
		this.setVideoOption("probesize", "32");
		this.setVideoOption("fflags", "nobuffer");
		this.setVideoOption("analyzeduration", "0");
		this.setVideoOption("rtbufsize", "0");
		this.context = context;
		logger = createLogger();
	}
	public void setViewer(ImageView ivViewer){
		viewer = ivViewer;
	}
	private void runVideo(){
		Frame frame;
		//OpenCVFrameConverter conv = new OpenCVFrameConverter.ToIplImage();

		try {
			this.start();
			while (!stop) {
				frame = this.grabFrame();
				if(frame == null) continue;
				if(frame.image != null){
					count++;
					logger.info("Frame count: "+count);
					AndroidFrameConverter av = new AndroidFrameConverter();
					final Bitmap mBitmap  = av.convert(frame);
					logger.info(mBitmap == null);
					context.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							viewer.setImageBitmap(mBitmap);
							logger.info("Viewed");
						}
					});
					frame = null;
				}
			}
			this.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void play(){
		Thread th = new Thread(new Runnable(){
			@Override
			public void run() {
				runVideo();
			}});
		//th.setPriority(Thread.MAX_PRIORITY);
		th.start();
	}
	public void stop(){
		this.stop = true;
		logger.info("Visible cadrs: " + String.valueOf(count));
	}
    public static IplImage rotateImage(IplImage img) {
        IplImage img_rotate = IplImage.create(img.height(), img.width(),  img.depth(), img.nChannels());
        opencv_core.cvTranspose(img, img_rotate);
        opencv_core.cvFlip(img_rotate, img_rotate, 1);
        return img_rotate;
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
		Logger logger = Logger.getLogger(String.valueOf(VSPlayer.class));
		return logger;
	}
}
