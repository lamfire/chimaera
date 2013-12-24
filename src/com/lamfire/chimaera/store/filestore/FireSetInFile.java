package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.code.MD5;
import com.lamfire.code.MurmurHash;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FireSetInFile implements FireSet {
	private final Map<String, byte[]> map;
    private final List<String> index;
    private final Lock lock = new ReentrantLock();
    private FileStore store;
    private String name;

    public FireSetInFile(FileStore store,String name){
        this.store = store;
        this.name = name;
        this.map = store.getHashMap(name);
        this.index = store.getLinkedList(name+"_index");
    }

	static String hash(byte[] bytes) {
		return MD5.hash(bytes) + ":" + MurmurHash.hash32(bytes, 1001);
	}

    public Iterator<byte[]> iterator(){
        return map.values().iterator();
    }

	@Override
	public void add(byte[] value) {
		try {
            String hash = hash(value);
            map.put(hash, value);
            index.add(hash);
            store.cacheOrFlush();
		} finally {
		}
	}

	@Override
	public byte[] get(int index) {
        lock.lock();
		try {
			String hash = this.index.get(index);
            return map.get(hash);
		} finally {
            lock.unlock();
		}
	}

    @Override
    public List<byte[]> gets(int fromIndex,int size) {
        List<byte[]> list = new ArrayList<byte[]>();
        lock.lock();
        try {
            for(int i = fromIndex;i< fromIndex + size;i++){
                byte[] bytes = get(i);
                list.add(bytes);
            }
        } finally {
            lock.unlock();
        }
        return list;
    }

    @Override
    public boolean exists(byte[] bytes) {
        return map.containsKey(hash(bytes));
    }

    @Override
	public int size() {
		return map.size();
	}

	@Override
	public byte[] remove(int index) {
		byte[] bytes = get(index);
		if (bytes != null) {
			remove(bytes);
            store.cacheOrFlush();
		}
		return bytes;
	}

	@Override
	public void clear() {
        lock.lock();
		try {
            map.clear();
            index.clear();
            store.flush();
		} finally {
            lock.unlock();
		}
	}

	@Override
	public byte[] remove(byte[] value) {
        lock.lock();
		try {
            String hash = hash(value);
            map.remove(hash);
            index.remove(hash);
            store.cacheOrFlush();
            return value;
		} finally {
            lock.unlock();
		}
	}

}
