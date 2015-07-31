package com.placelook.commands;

import java.util.ArrayList;

public class Actions {
	public static final String GO_FORWARD = "go_forward";
	public static final String GO_BACKWARD = "go_backward";
	public static final String GO_LEFT = "go_left";
	public static final String GO_RIGHT = "go_right";
	public static final String LOOK_UP = "look_up"; 
	public static final String LOOK_DOWN = "look_down";
	public static final String LOOK_LEFT = "look_left";
	public static final String LOOK_RIGHT = "look_right";
	public static final String STOP = "stop";
	public static final String TEXT = "text";
	public static final String GEO = "geo";
	public static final String BALANCE = "balance";
	public static ArrayList<String> toArrayList(){
		ArrayList<String> res = new ArrayList();
		res.add(BALANCE);
		res.add(GEO);
		res.add(GO_BACKWARD);
		res.add(GO_FORWARD);
		res.add(GO_LEFT);
		res.add(GO_RIGHT);
		res.add(LOOK_DOWN);
		res.add(LOOK_LEFT);
		res.add(LOOK_RIGHT);
		res.add(LOOK_UP);
		res.add(STOP);
		res.add(TEXT);
		return res;
	}
}
