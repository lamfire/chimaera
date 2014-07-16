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
    private Sequence counter;
    private final Lock lock = new ReentrantLock();

    public BDBFireMap(BDBEngine engine, String name) {
        this.engine = engine;
        this.name = name;
        this.map = engine.getMap(name);
        this.counter = engine.getSequence(name+"_COUNTER");
    }

    @Override
    public void put(String key, byte[] value) {
        try {
            lock.lock();
            boolean exists = map.containsKey(key);
            map.put(key, value);
            if(!exists){
                counter.increment();
            }
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
        return (int)counter.get();
    }

    @Override
    public void remove(String key) {
        try {
            lock.lock();
            boolean exists = map.containsKey(key);
            map.remove(key);
            if(exists){
                counter.increment(-1);
            }
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
            counter.set(0);
        } finally {
            lock.unlock();

        }
    }

}
