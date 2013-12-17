package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireQueue;

import java.util.Iterator;
import java.util.LinkedList;

public class FireQueueInMemory implements FireQueue {
	
	private final LinkedList<byte[]> queue = new LinkedList<byte[]>();

	@Override
	public void pushLeft(byte[] value) {
		queue.addFirst(value);
	}

    public Iterator<byte[]> iterator(){
        return queue.iterator();
    }

	@Override
	public byte[] popLeft() {
        if(queue.isEmpty()){
            return null;
        }
		return queue.removeFirst();
	}

	@Override
	public void pushRight(byte[] value) {
		queue.addLast(value);
	}

	@Override
	public byte[] popRight() {
        if(queue.isEmpty()){
            return null;
        }
		return queue.removeLast();
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public void clear() {
		queue.clear();
	}

}
