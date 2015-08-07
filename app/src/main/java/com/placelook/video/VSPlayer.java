package com.placelook.video;

import java.io.File;

import org.apache.log4j.Logger;
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
import android.view.SurfaceView;
import android.widget.ImageView;


public class VSPlayer extends FFmpegFrameGrabber{
//	private int sampleAudioRateInHz = 44100;
	private int sampleAudioRateInHz = 16000;	
//	private int frameRate = 24;
	private int frameRate = 16;	
	private ImageView viewer;
	private boolean stop = false;
	private Activity context;
	private long count = 0;
	private Logger logger;
	public VSPlayer(Activity context, String stream) {
		super(stream);
		this.init();
		this.setFormat("flv");
		this.setSampleRate(sampleAudioRateInHz);
		this.setFrameRate(frameRate);
		this.context = context;
		logger = Logger.getLogger(VSPlayer.class);
	}
	public void setViewer(ImageView ivViewer){
		viewer = ivViewer;
	}
	private void runVideo(){
		Frame frame;
		//OpenCVFrameConverter conv = new OpenCVFrameConverter.ToIplImage();
		AndroidFrameConverter av = new AndroidFrameConverter();
		try {
			this.start();
			while (!stop) {
				frame = this.grab();
				if(frame == null) continue;
				if(frame.image != null){
					count++;
					//IplImage mFrame1 = conv.convertToIplImage(frame);//IplImage.create(frame.imageWidth,frame.imageHeight, opencv_core.IPL_DEPTH_8U,4);
					//IplImage imgRotated = this.rotateImage(mFrame1);
					//final Bitmap mBitmap = Bitmap.createBitmap(imgRotated.width(), imgRotated.height(),Bitmap.Config.ARGB_8888);
					//mBitmap.copyPixelsFromBuffer(imgRotated.getByteBuffer());
					final Bitmap mBitmap  = av.convert(frame);
					context.runOnUiThread(new Runnable(){
						@Override
						public void run() {
							viewer.setImageBitmap(mBitmap);					}
					});
					//mFrame1.release();
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
		logger.info("Visible cadrs: "+String.valueOf(count));
	}
    public static IplImage rotateImage(IplImage img) {
        IplImage img_rotate = IplImage.create(img.height(), img.width(),  img.depth(), img.nChannels());
        opencv_core.cvTranspose(img, img_rotate);
        opencv_core.cvFlip(img_rotate, img_rotate, 1);
        return img_rotate;
    }
}
