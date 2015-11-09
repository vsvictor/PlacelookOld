package com.placelook.utils;

import android.graphics.Bitmap;
import android.graphics.YuvImage;

/**
 * Created by victor on 05.10.15.
 */
public class YUVPicture{
    static {
        System.loadLibrary("yuv");
        System.loadLibrary("yuvpicture");
    }
/*
    public YUVPicture(byte[] yuv, int format, int width, int height, int[] strides) {
        super(yuv, format, width, height, strides);
        this.format = format;
    }
    public YUVPicture scale(int dst_width, int dst_height, int channels){
        byte[] res = scale_native(getYuvData(),getWidth(),getHeight(),dst_width,dst_height,channels);
        return new YUVPicture(res,format,dst_width,dst_height,null);
    }
    private native byte[] scale_native(byte[] src, int src_width, int src_height, int dst_width, int dst_height, int channels);
*/
    public native byte[] convert(byte[] nv21, int w, int h, int type);
}
