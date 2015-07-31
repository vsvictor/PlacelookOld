package com.placelook.video;


import org.apache.log4j.Logger;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import android.os.AsyncTask;

import com.placelook.data.SyncQueue;

public class VSRecorder extends FFmpegFrameRecorder {
	private VSRecorder instance;
	private Thread th;
	private volatile IplImage im;
	private boolean stoped = false;
	private Logger logger;
	private int count = 0;
	private SyncQueue list;
	private static VSRecorder rec;

	public VSRecorder(String filename, int width, int height, int audioChannels) {
		super(filename, width, height, audioChannels);
		list = new SyncQueue();
		logger = Logger.getLogger(VSRecorder.class);
		rec = this;
	}

	@Override
	public void start() throws Exception {
		super.start();
		if (th != null && th.isAlive())
			return;
		th = new Thread(new Runnable() {
			@Override
			public void run() {
				th.setPriority(Thread.MAX_PRIORITY);
				//android.os.Process.setThreadPriority(Thread.MAX_PRIORITY);
				while (!stoped) {
					Frame cl;
					if (list.isEmpty()) {
						try {
							th.sleep(100);
							logger.info("sleep...");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					logger.info(list.size());
					cl = (Frame) list.pop();
					if (cl != null) {
						try {
							record(cl);
							count++;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return;
			}
		});
		// th.setPriority(Thread.MAX_PRIORITY);
		th.start();
	}

	@Override
	public void stop() throws Exception {
		stoped = true;
		logger.info("Send cadrs: " + String.valueOf(count));
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// if(!im.isNull()) im.release();
		try {
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// super.stop();
		list.clear();
	}

	public void stopAcync() {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}

	public void toStream(Frame aFrame) {
		if (aFrame != null && aFrame.image != null) {
			list.push(aFrame);
		}
	}
}
