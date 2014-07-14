package com.lamfire.chimaera.store.bdbstore;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.utils.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 持久化的FireMap类，该对象中的数据将会被持久化到文件中。
 */
public class BDBFireMap implements FireMap {
    private BDBEngine engine;
    private String name;
    private Map<String, byte[]> map;
    private final Lock lock = new ReentrantLock();

    public BDBFireMap(BDBEngine engine, String name) {
        this.engine = engine;
        this.name = name;
        this.map = engine.getMap(name);
    }

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
    public int size() {
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
    public synchronized void clear() {
        try {
            lock.lock();
            map.clear();
        } finally {
            lock.unlock();

        }
    }

}