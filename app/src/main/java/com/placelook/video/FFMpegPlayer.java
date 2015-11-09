package com.placelook.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by victor on 04.11.15.
 */
public class FFMpegPlayer {
    private static final String TAG = "FFMpegPlayer";
    private Context context;
    private static FFMpegPlayer instance;
    private boolean running;
    private volatile ArrayBlockingQueue<Frame> data;
    private AndroidFrameConverter ac;
    private SurfaceHolder surface;
    private String source;
    private int width;
    private int height;
    private FFMpegRotate rotate;
    private int sampleRate;
    private int frameRate;
    public FFMpegPlayer(Context context, String source) {
        this.context = context;
        running = false;
        surface = null;
        this.source = source;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        this.width = display.getWidth();  // deprecated
        this.height = display.getHeight();
        this.rotate = FFMpegRotate.ROTATE_0;
        this.frameRate = 24;
        ac = new AndroidFrameConverter();
        instance = this;
        data = new ArrayBlockingQueue<Frame>(512, false);
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public void setSurfaceHolder(SurfaceHolder surface) {
        this.surface = surface;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setRotate(FFMpegRotate angle) {
        this.rotate = angle;
    }

    public void play() {
        running = true;
        runReader();
        runDrawer();
    }

    private void runReader() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(source);
                grabber.setFormat("flv");
                grabber.setSampleRate(instance.sampleRate);
                grabber.setFrameRate(instance.frameRate);
                grabber.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                grabber.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
                grabber.setVideoOption("tune", "zerolatency");
                grabber.setVideoOption("cpuflags", "vfpv3");
                grabber.setVideoOption("cpuflags", "neon");
                grabber.setVideoOption("probesize", "32");
                grabber.setVideoOption("fflags", "nobuffer");
                grabber.setVideoOption("analyzeduration", "0");
                grabber.setVideoOption("rtbufsize", "0");
                running = true;
                Log.i(TAG, "Running reader");

                try {
                    grabber.start();
                    while (running) {
                        Frame f = grabber.grab();
                        if( f == null){
                            Log.i(TAG, "Null frame");
                            continue;
                        }
                        try {
                            data.put(f);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "Frame grabbed");
                    }
                } catch (FrameGrabber.Exception e) {
                    Log.i(TAG, "FrameGrabber.Exception");
                    e.printStackTrace();
                }
            }
        });
        th.setPriority(Thread.MAX_PRIORITY);
        th.start();
    }

    private void runDrawer() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                Rect dst = new Rect(0, 0, instance.width, instance.height);
                Log.i(TAG, "Running drawer");
                while (running) {
                    Frame current = null;
                    try {
                        current = data.take();
                        Bitmap tmp = ac.convert(current);
                        if (tmp == null) continue;
                        Log.i(TAG, "Bitmap not null");
                        if (rotate != FFMpegRotate.ROTATE_360 && rotate != FFMpegRotate.ROTATE_0) {
                            int angle = 0;
                            switch (rotate) {
                                case ROTATE_90: {
                                    angle = 90;
                                    break;
                                }
                                case ROTATE_180: {
                                    angle = 190;
                                    break;
                                }
                                case ROTATE_270: {
                                    angle = 270;
                                    break;
                                }
                            }
                            Matrix m = new Matrix();
                            m.postRotate(angle);
                            bitmap = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m, false);
                            Log.i(TAG, "Bitmap rotated");
                        } else bitmap = Bitmap.createBitmap(tmp);
                        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        Canvas canvas = instance.surface.lockCanvas();
                        canvas.drawBitmap(bitmap, src, dst, new Paint());
                        instance.surface.unlockCanvasAndPost(canvas);
                        Log.i(TAG, "Bitmap drawed");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.setPriority(Thread.MAX_PRIORITY);
        th.start();
    }
}
