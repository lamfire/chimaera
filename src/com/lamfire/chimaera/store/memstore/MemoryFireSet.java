package com.lamfire.chimaera.store.memstore;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.code.MD5;
import com.lamfire.code.MurmurHash;

public class MemoryFireSet implements FireSet {

	private final Map<String, byte[]> store = new LinkedHashMap<String, byte[]>();
    private final Lock lock = new ReentrantLock();

	static String hash(byte[] bytes) {
		return MD5.hash(bytes) + ":" + MurmurHash.hash32(bytes, 1001);
	}

    public Iterator<byte[]> iterator(){
        return store.values().iterator();
    }

	@Override
	public void add(byte[] value) {
		try {
			store.put(hash(value), value);
		} finally {
		}
	}

	@Override
	public byte[] get(int index) {
        lock.lock();
		try {
			Iterator<Map.Entry<String, byte[]>> it = store.entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
                Map.Entry<String ,byte[]> entry = it.next();
				if (i++ == index) {
					return entry.getValue();
				}
			}
		} finally {
            lock.unlock();
		}
		return null;
	}

    @Override
    public List<byte[]> gets(int fromIndex,int size) {
        List<byte[]> list = new ArrayList<byte[]>();
        lock.lock();
        try {
            Iterator<Map.Entry<String, byte[]>> it = store.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Map.Entry<String ,byte[]> entry = it.next();
                if (i >= fromIndex) {
                    list.add(entry.getValue());
                    if(list.size() == size){
                        return list;
                    }
                }
                i++;
            }
        } finally {
            lock.unlock();
        }
        return list;
    }

    @Override
    public boolean exists(byte[] bytes) {
        return store.containsKey(hash(bytes));
    }

    @Override
	public int size() {
		return store.size();
	}

	@Override
	public byte[] remove(int index) {
		byte[] bytes = get(index);
		if (bytes != null) {
			remove(bytes);
		}
		return bytes;
	}

	@Override
	public void clear() {
        lock.lock();
		try {
			store.clear();
		} finally {
            lock.unlock();
		}
	}

	@Override
	public byte[] remove(byte[] value) {
        lock.lock();
		try {
			store.remove(hash(value));
            return value;
		} finally {
            lock.unlock();
		}
	}

}
