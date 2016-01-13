package com.placelook.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.ImageView;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;

/**
 * Created by victor on 04.11.15.
 */
public class FFMpegPlayer {
    private static final String TAG = "FFMpegPlayer";
    private Context context;
    private Activity act;
    private static FFMpegPlayer instance;
    private boolean running;
    private volatile ArrayBlockingQueue<Frame> data;
    private volatile ArrayBlockingQueue<Bitmap> bitmaps;
    private volatile ArrayBlockingQueue<Frame> audio;
    private volatile ArrayBlockingQueue<short[]> intaudio;
    //private SurfaceHolder surface;
    private ImageView iv;
    private String source;
    private int width;
    private int height;
    private FFMpegRotate rotate;
    private int sampleRate = 8000;
    private int frameRate;
    private AudioTrack track;
    private final int channels = 1;
    private Handler handler;


    /*
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
            instance = this;
            data = new ArrayBlockingQueue<Frame>(512, false);
            bitmaps = new ArrayBlockingQueue<Bitmap>(512, false);
            audio = new ArrayBlockingQueue<Frame>(512, false);
            intaudio = new ArrayBlockingQueue<short[]>(512, false);

            final int af = (channels == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO);
            int minSize = AudioTrack.getMinBufferSize(sampleRate, af, AudioFormat.ENCODING_PCM_16BIT);
            track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channels == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT, minSize / 2, AudioTrack.MODE_STREAM);
            track.play();
            handler = new Handler();
        }
    */
    public FFMpegPlayer(Activity act, String source){
        this.context = act;
        this.act = act;
        running = false;
        //surface = null;
        this.source = source;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        this.width = display.getWidth();  // deprecated
        this.height = display.getHeight();
        this.rotate = FFMpegRotate.ROTATE_0;
        this.frameRate = 24;
        instance = this;
        data = new ArrayBlockingQueue<Frame>(512, false);
        bitmaps = new ArrayBlockingQueue<Bitmap>(512, false);
        audio = new ArrayBlockingQueue<Frame>(512, false);
        intaudio = new ArrayBlockingQueue<short[]>(512, false);

        final int af = (channels == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO);
        int minSize = AudioTrack.getMinBufferSize(sampleRate, af, AudioFormat.ENCODING_PCM_16BIT);
        track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channels == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT, minSize / 2, AudioTrack.MODE_STREAM);
        track.play();
        handler = new Handler();
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }
/*
    public void setSurfaceHolder(SurfaceHolder surface) {
        this.surface = surface;
    }
*/
    public void setDrawer(ImageView iv){
        this.iv = iv;
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
        runPreparatorVideo();
        runDrawer();
        runPreparatorAudio();
        runAudio();
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
                grabber.setSampleFormat(avcodec.AV_CODEC_ID_AAC);
                grabber.setAudioChannels(1);
                grabber.setVideoOption("tune", "zerolatency");
                grabber.setVideoOption("cpuflags", "vfpv3");
                grabber.setVideoOption("cpuflags", "neon");
                grabber.setVideoOption("probesize", "32");
                grabber.setVideoOption("fflags", "nobuffer");
                grabber.setVideoOption("analyzeduration", "0");
                grabber.setVideoOption("rtbufsize", "0");
/*
                grabber.setVideoOption("audio-sync", "");

                grabber.setAudioOption("bsf:a", "aac_adtstoasc");
                grabber.setAudioOption("desync", "10");
*/
                running = true;
                Log.i(TAG, "Running reader");

                try {
                    grabber.start();
                    while (running) {
                        final Frame f = grabber.grab();
                        if (f == null) {
                            Log.i(TAG, "Null frame");
                            continue;
                        }
                        try {
                            if (f.samples != null) audio.put(f);
                            if (f.image != null) data.put(f);
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

    private void runPreparatorVideo() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                final Convertor vc = new Convertor();
                while (running) {
                    try {
                        final Bitmap bitmap;
                        Frame current = null;
                        if(data.size()>0)current = data.take();
                        else Thread.yield();
                        if (current != null && current.image != null) {
                            Bitmap tmp = vc.toBitmap(current);
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
                                        angle = 180;
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
                            bitmaps.put(bitmap);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.setPriority(9);
        th.start();
    }

    private void runDrawer() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                Rect dst = new Rect(0, 0, instance.width, instance.height);
                Paint p = new Paint();
                Log.i(TAG, "Running drawer");
                while (running) {
                    try {

                        if(bitmaps.size() > 0) bitmap = bitmaps.take();
                        else Thread.yield();
                        if (bitmap == null) continue;
/*                        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        Canvas canvas = instance.surface.lockCanvas();
                        canvas.drawBitmap(bitmap, src, dst, p);
                        instance.surface.unlockCanvasAndPost(canvas);
                        Log.i(TAG, "Bitmap drawed");
*/                      final Bitmap bp = bitmap.copy(bitmap.getConfig(), true);
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv.setImageBitmap(bp);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.setPriority(8);
        th.start();
    }

    private void runPreparatorAudio() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("AUDIO", "Audio preparator running");
                final Convertor ac = new Convertor();
                while (running) {
                    try {
                        Frame au = null;
                        if(audio.size()>0) au = audio.take();
                        else Thread.yield();
                        short[] sh = ac.toAudio(au, instance.channels);
                        if (sh == null) continue;
                        intaudio.put(sh);
                        Log.i("AUDIO", "Audio preparator sended");
                    } catch (InterruptedException e) {
                        Log.i("AUDIO", "Audio preparator creshed");
                        e.printStackTrace();
                    }
                }
            }
        });
        th.setPriority(9);
        th.start();
    }

    private void runAudio() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("AUDIO", "Audio running");
                while (running) {
                    try {
                        short[] sh = null;
                        if(intaudio.size() > 0) sh = intaudio.take();
                        else Thread.yield();
                        if(sh == null) continue;
                        if(track.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) track.play();
                        track.write(sh, 0, sh.length);
                        Log.i("AUDIO", "Audio play");
                    } catch (InterruptedException e) {
                        Log.i("AUDIO", "Audio crashed");
                        e.printStackTrace();
                    }
                }
            }
        });
        th.setPriority(8);
        th.start();
    }
}
