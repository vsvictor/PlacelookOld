package com.placelook.data;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SyncQueue {
	private ConcurrentLinkedQueue list;

	public SyncQueue(){
		list = new ConcurrentLinkedQueue();
	}
	
	public void push(Object v) {
		list.add(v);
	}
	public Object pop() {
		return list.poll();
	}

	public boolean isEmpty() {
		return ((list == null)?true:list.isEmpty());
	}
	public long size(){
		return list.size();
	}
	public void clear(){
		list.removeAll(list);
	}	
}
