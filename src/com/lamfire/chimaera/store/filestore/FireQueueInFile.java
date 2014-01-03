package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireQueue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FireQueueInFile implements FireQueue {

    private final List<byte[]> list;
    private FileStore store;
    private String name;
    private final Lock lock = new ReentrantLock();

    public FireQueueInFile(FileStore store,String name){
        this.store = store;
        this.name = name;
        this.list = store.getLinkedList(name);
    }

	@Override
	public void push(byte[] value) {
        lock.lock();
        try{
		    list.add(value);
        }finally{
            lock.unlock();
        }
	}

    public Iterator<byte[]> iterator(){
        return list.iterator();
    }

	@Override
	public byte[] pop() {
        if(list.isEmpty()){
            return null;
        }
        lock.lock();
        try{
		    return list.remove(0);
        }finally{
            lock.unlock();
        }
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public void clear() {
        lock.lock();
        try{
		list.clear();
        }finally{
            lock.unlock();
        }
	}

}
