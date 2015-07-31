package com.placelook.pages.fragments.client;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

/**
 * Created by victor on 20.07.15.
 */
public class ExMapView extends MapView {
    public ExMapView(Context context) {
        super(context);
    }
    public ExMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ExMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public ExMapView(Context context, GoogleMapOptions options) {
        super(context, options);
    }
}
