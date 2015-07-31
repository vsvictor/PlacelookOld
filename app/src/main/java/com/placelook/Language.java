package com.placelook;

public class Language {
	private String sSmallLanguage;
	private String sLongLanguage;
	
	public Language(){
		sSmallLanguage = "en";
		sLongLanguage = "English";
	}
	public Language(String sSmall, String sLong){
		sSmallLanguage = sSmall;
		sLongLanguage = sLong;
	}
	public void setLanguage(Language lang){
		this.sSmallLanguage = lang.sSmallLanguage;
		this.sLongLanguage = lang.sLongLanguage;
	}
	public Language getLanguage(){
		return this;
	}
	public String getShortLanguage(){
		return this.sSmallLanguage;
	}
	public String getLongLanguage(){
		return this.sLongLanguage;
	}
}
