package com.placelook.utils;

import org.bytedeco.javacpp.presets.opencv_core;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by victor on 22.06.15.
 */
public class DateTimeOperator {
    public static String secondToHHMMSS(int seconds){
        int sBegin = seconds;
        int hours = sBegin/3600;
        sBegin = sBegin-(hours*3600);
        int minutes = sBegin/60;
        sBegin = sBegin - (minutes*60);
        int sec = sBegin;
        String sHours = String.valueOf(hours);
        if(sHours.length()<2) sHours = "0"+sHours;
        String sMin = String.valueOf(minutes);
        if(sMin.length()<2) sMin = "0"+sMin;
        String sSec = String.valueOf(sec);
        if(sSec.length()<2) sSec = "0"+sSec;
        return sHours+":"+sMin+":"+sSec;
    }
    public static String dateToHHMM(Date d){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return "";
    }
    public static String dateToMMDDYYYY(Date d){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return "";
    }

}
