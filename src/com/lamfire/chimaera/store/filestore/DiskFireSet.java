package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.code.MD5;
import com.lamfire.code.MurmurHash;
import org.apache.jdbm.Serialization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiskFireSet implements FireSet {
    private final Map<String, byte[]> map;
    private final List<String> index;
    private final Lock lock = new ReentrantLock();
    private StoreEngine engine;
    private String name;

    public DiskFireSet(StoreEngine engine, String name) {
        this.engine = engine;
        this.name = name;
        this.map = engine.getHashMap(name);
        this.index = engine.getLinkedList(name + "_index", new Serialization());
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
            map.put(hash, value);
            index.add(hash);
            engine.cacheOrFlush();
        } finally {
            lock.unlock();
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
    public List<byte[]> gets(int fromIndex, int size) {
        List<byte[]> list = new ArrayList<byte[]>();
        lock.lock();
        try {
            for (int i = fromIndex; i < fromIndex + size; i++) {
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
        lock.lock();
        try {
            byte[] bytes = get(index);
            if (bytes != null) {
                remove(bytes);
                engine.cacheOrFlush();
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
            index.clear();
            engine.cacheOrFlush();
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
            engine.cacheOrFlush();
            return value;
        } finally {
            lock.unlock();
        }
    }

}
