package com.placelook.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;
import com.placelook.data.IntStringPair;

public class Operator extends IntStringPair {
	private String country;
	private Integer idCity;
	private Double width;
	private Double height;
	private Boolean gps;
	private Marker marker;
	
	public Operator(){
		super();
		this.country = "";
		this.idCity = new Integer(0);
		this.width = new Double(0.0);
		this.height = new Double(0.0);
		this.gps = new Boolean(false);
		marker = null;
	}
	public Operator(int id, String name, String country, int idCity, double width, double height, boolean isGPS){
		super(id, name);
		this.country = country;
		this.idCity = new Integer(idCity);
		this.width = new Double(width);
		this.height = new Double(height);
		this.gps = new Boolean(isGPS);
		marker = null;
	}
	public String getCountry(){return this.country;}
	public int getIDCity(){return this.idCity;}
	public double getWidth(){return this.width;}
	public double getHeigth(){return this.height;}
	public boolean isGPS(){return this.gps;}
	public void setMarker(Marker aMarker){
		this.marker = aMarker;
	}
	public Marker getMarker(){
		return marker;
	}
}
