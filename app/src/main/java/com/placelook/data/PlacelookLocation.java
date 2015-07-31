package com.placelook.data;


import com.google.android.gms.maps.model.LatLng;

public class PlacelookLocation {
	private String country;
	private int idCity;
	private double width;
	private double height;
	private boolean gps;
	
	public PlacelookLocation(){
		this.country ="";
		this.idCity = 0;
		this.width = 0.0;
		this.height = 0.0;
		this.gps = false;
	}
	public void setCountry(String country){
		this.country = country;
	}
	public String getCountry(){
		return this.country;
	}
	public void setIDCity(int idCity){
		this.idCity = idCity;
	}
	public int getIDCity(){
		return this.idCity;
	}
	public void setLatitude(double lat){
		this.width = lat;
	}
	public double getLatitude(){
		return this.width;
	}
	public void setLongitude(double lon){
		this.height = lon;
	}
	public double getLongitude(){
		return this.height;
	}
	public void setGPS(boolean gps){
		this.gps = gps;
	}
	public boolean getGPS(){
		return this.gps;
	}
	public LatLng getLocation(){
		return new LatLng(this.width, this.height);
	}
}
