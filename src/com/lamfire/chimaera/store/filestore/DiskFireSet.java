package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.code.MD5;
import com.lamfire.code.MurmurHash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiskFireSet implements FireSet {
    private final Map<String, byte[]> map;

    private final Lock lock = new ReentrantLock();
    private DiskDatabase engine;
    private String name;

    public DiskFireSet(DiskDatabase engine, String name) {
        this.engine = engine;
        this.name = name;
        this.map = engine.getHashMap(name);
    }

    static String hash(byte[] bytes) {
        return MD5.hash(bytes) + ":" + MurmurHash.hash32(bytes, 1001);
    }

    public Iterator<byte[]> iterator() {
        return map.values().iterator();
    }

    @Override
    public void add(byte[] value) {
        try {
            lock.lock();
            String hash = hash(value);
            if(! map.containsKey(hash)){
                map.put(hash, value);
                engine.flush();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] get(int index) {
        lock.lock();
        try {
            int i=0;
            for(Map.Entry<String, byte[]> e: map.entrySet()){
                if(i == index){
                     return e.getValue();
                }
                i++;
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        List<byte[]> list = new ArrayList<byte[]>();
        lock.lock();
        try {
            int i=0;
            for(Map.Entry<String, byte[]> e: map.entrySet()){
                if(i >= fromIndex){
                   list.add(e.getValue());
                    if(list.size() == size){
                        break;
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
        return map.containsKey(hash(bytes));
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public byte[] remove(int index) {
        lock.lock();
        try {
            byte[] bytes = get(index);
            if (bytes != null) {
                remove(bytes);
                engine.flush();
            }
            return bytes;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            map.clear();
            engine.flush();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] remove(byte[] value) {
        lock.lock();
        try {
            String hash = hash(value);
            if(map.remove(hash)!=null){
                engine.flush();
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

}
