package com.placelook.data;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SyncQueue {
	private ConcurrentLinkedQueue list;
	private SyncQueueOnChange listener;

	public SyncQueue(){
		list = new ConcurrentLinkedQueue();
	}
	
	public void push(Object v) {
		list.add(v);
        if(listener != null) listener.onAdded();
	}
	public Object pop() {
		Object obj = list.poll();
		if(listener != null) listener.onDeleted();
		return obj;
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
	public void setOnAdded(SyncQueueOnChange add){this.listener = add;}
}
