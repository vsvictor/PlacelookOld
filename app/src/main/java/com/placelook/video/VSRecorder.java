package com.placelook.video;

import com.placelook.data.SyncQueue;
import com.placelook.data.SyncQueueOnChange;

//import org.apache.log4j.Logger;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import android.os.AsyncTask;
import android.util.Log;

public class VSRecorder extends FFmpegFrameRecorder {
	private VSRecorder instance;
	private Thread th;
	private volatile IplImage im;
	private boolean stoped = false;
//	private Logger logger;
	private int count = 0;
	private SyncQueue list;
	private static VSRecorder rec;

	public VSRecorder(String filename, int width, int height, int audioChannels) {
		super(filename, width, height, audioChannels);
		list = new SyncQueue();
//		logger = Logger.getLogger(VSRecorder.class);
		rec = this;
	}

	@Override
	public void start() throws Exception {
		super.start();
		list.setOnAdded(new SyncQueueOnChange() {
			@Override
			public void onAdded() {
				Log.i("VSRecorder","Size list: "+list.size());
			}

			@Override
			public void onDeleted() {
				Log.i("VSRecorder","Size list: "+list.size());
			}
		});
		if (th != null && th.isAlive())
			return;
		th = new Thread(new Runnable() {
			@Override
			public void run() {
			while (!stoped) {
				if (!list.isEmpty()) try {
					rec.record((Frame) list.pop());
				} catch (Exception e) {
					e.printStackTrace();
				}
				else try {
					Thread.sleep(100);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			}
		});
		th.setPriority(Thread.MAX_PRIORITY);
		th.start();
	}

	@Override
	public void stop() throws Exception {
		stoped = true;
//		logger.info("Send cadrs: " + String.valueOf(count));
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
		if (aFrame != null) {
			list.push(aFrame);
		}
	}
}
