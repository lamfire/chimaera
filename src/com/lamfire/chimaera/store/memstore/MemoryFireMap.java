package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.utils.Lists;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryFireMap implements FireMap {
    private final Lock lock = new ReentrantLock();
    private final Map<String, byte[]> map = new LinkedHashMap<String, byte[]>();

    @Override
    public void put(String key, byte[] value) {
        try {
            lock.lock();
            map.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<String> keys() {
        try {
            lock.lock();
            List<String> list = Lists.newArrayList(map.keySet());
            return list;
        } finally {
            lock.unlock();
        }
    }

    public Set<String> getKeys() {
        return map.keySet();
    }

    public Collection<byte[]> getValues() {
        return map.values();
    }

    @Override
    public byte[] get(String key) {
        return map.get(key);
    }


    @Override
    public long size() {
        return map.size();
    }

    @Override
    public void remove(String key) {
        try {
            lock.lock();
            map.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean exists(String key) {
        return map.containsKey(key);
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            map.clear();
        } finally {
            lock.unlock();
        }
    }

}
