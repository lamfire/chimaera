package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireQueue;

import java.util.Iterator;
import java.util.LinkedList;

public class FireQueueInMemory implements FireQueue {
	
	private final LinkedList<byte[]> queue = new LinkedList<byte[]>();

	public void push(byte[] value) {
		queue.addLast(value);
	}

    public Iterator<byte[]> iterator(){
        return queue.iterator();
    }

	@Override
	public byte[] pop() {
        if(queue.isEmpty()){
            return null;
        }
		return queue.removeFirst();
	}

    public byte[] peek() {
        if(queue.isEmpty()){
            return null;
        }
        return queue.peekFirst();
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
