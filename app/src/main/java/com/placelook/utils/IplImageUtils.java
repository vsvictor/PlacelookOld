package com.placelook.utils;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Created by victor on 05.10.15.
 */
public class IplImageUtils {
    public static opencv_core.IplImage rotate(opencv_core.IplImage image, double angle) {
        opencv_core.IplImage copy = opencv_core.cvCloneImage(image);

        opencv_core.IplImage rotatedImage = opencv_core.cvCreateImage(opencv_core.cvGetSize(copy), copy.depth(), copy.nChannels());
        opencv_core.CvMat mapMatrix = opencv_core.cvCreateMat( 2, 3, opencv_core.CV_32FC1 );

        //Define Mid Point
        opencv_core.CvPoint2D32f centerPoint = new opencv_core.CvPoint2D32f();
        centerPoint.x(copy.width()/2);
        centerPoint.y(copy.height()/2);

        //Get Rotational Matrix
        opencv_imgproc.cv2DRotationMatrix(centerPoint, angle, 1.0, mapMatrix);

        //Rotate the Image
        opencv_imgproc.cvWarpAffine(copy, rotatedImage, mapMatrix, opencv_imgproc.CV_INTER_CUBIC +  opencv_imgproc.CV_WARP_FILL_OUTLIERS, opencv_core.cvScalarAll(170));
        opencv_core.cvReleaseImage(copy);
        opencv_core.cvReleaseMat(mapMatrix);
        return rotatedImage;
    }
}
