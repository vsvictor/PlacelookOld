package com.placelook.utils;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;

import java.nio.ByteBuffer;

/**
 * Created by victor on 20.10.15.
 */
public class VSFrameConvertor extends AndroidFrameConverter {
    public Frame convert(byte[] data, int width, int height, int channels) {
        //if (frame == null || frame.imageWidth != width
        //        || frame.imageHeight != height || frame.imageChannels != 3) {
        //    frame = new Frame(width, height, Frame.DEPTH_UBYTE, 3);
        //}
        if(frame == null) frame = new Frame(width, height, Frame.DEPTH_UBYTE, channels);
        ByteBuffer out = (ByteBuffer)frame.image[0];
        int stride = frame.imageStride;

        // ported from https://android.googlesource.com/platform/development/+/master/tools/yuv420sp2rgb/yuv420sp2rgb.c
        int offset = height * width;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int Y = data[i * width + j] & 0xFF;
                int V = data[offset + (i/2) * width + 2 * (j/2)    ] & 0xFF;
                int U = data[offset + (i/2) * width + 2 * (j/2) + 1] & 0xFF;

                // Yuv Convert
                Y -= 16;
                U -= 128;
                V -= 128;

                if (Y < 0)
                    Y = 0;

                // R = (int)(1.164 * Y + 2.018 * U);
                // G = (int)(1.164 * Y - 0.813 * V - 0.391 * U);
                // B = (int)(1.164 * Y + 1.596 * V);

                int B = (int)(1192 * Y + 2066 * U);
                int G = (int)(1192 * Y - 833 * V - 400 * U);
                int R = (int)(1192 * Y + 1634 * V);

                R = Math.min(262143, Math.max(0, R));
                G = Math.min(262143, Math.max(0, G));
                B = Math.min(262143, Math.max(0, B));

                R >>= 10; R &= 0xff;
                G >>= 10; G &= 0xff;
                B >>= 10; B &= 0xff;

                out.put(i * stride + 3 * j,     (byte)B);
                out.put(i * stride + 3 * j + 1, (byte)G);
                out.put(i * stride + 3 * j + 2, (byte)R);
            }
        }
        return frame;
    }
}
