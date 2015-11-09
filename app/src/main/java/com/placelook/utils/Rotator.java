package com.placelook.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;


/**
 * Created by victor on 25.08.15.
 */
public class Rotator {
    public static Bitmap rotate(Bitmap arg, int angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(arg, 0, 0, arg.getWidth(), arg.getHeight(), matrix, true);
    }
}
