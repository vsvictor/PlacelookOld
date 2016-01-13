package com.placelook.video;

import android.graphics.Bitmap;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;

import java.nio.Buffer;
import java.nio.FloatBuffer;

/**
 * Created by victor on 05.01.16.
 */
public class Convertor extends AndroidFrameConverter {
    private FloatBuffer fb;
    private float[] smpls;

    private short[] fillBuffer( float[] samples ) {
        //if( buffer.length != samples.length )
        short[] buffer = new short[samples.length];
        for( int i = 0; i < samples.length; i++ ) buffer[i] = (short)(samples[i] * Short.MAX_VALUE);
        return buffer;
    }

    public Bitmap toBitmap(Frame f){
        return super.convert(f);
    }
    public short[] toAudio(Frame f, int channels){
        if(f == null || f.samples == null || f.samples[0] == null) return null;
        if(channels == 1){
            Buffer b=f.samples[0];
            fb = (FloatBuffer)b;
            fb.rewind();
            smpls=new float[fb.capacity()];
            fb.get(smpls);
        }
        else if(channels==2){
            FloatBuffer b1=(FloatBuffer) f.samples[0];
            FloatBuffer b2=(FloatBuffer) f.samples[1];
            smpls=new float[b1.capacity()+b2.capacity()];
            for(int i=0;i<b1.capacity();i++){
                smpls[2*i]=b1.get(i);  smpls[2*i+1]=b2.get(i);
            }
        }
        return fillBuffer(smpls);
    }
}
