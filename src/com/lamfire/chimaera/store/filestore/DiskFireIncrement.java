package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.Item;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 上午11:53
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireIncrement implements FireIncrement {
    private final Lock lock = new ReentrantLock();
    private Map<String, Item> map;
    private DiskDatabase engine;
    private String name;

    public DiskFireIncrement(DiskDatabase engine, String name) {
        this.engine = engine;
        this.name = name;
        this.map = engine.getHashMap(name + "_MAP", new ItemSerializer());
    }

    @Override
    public void incr(String name) {
        incr(name,1);
    }

    private synchronized Item newItem(String name,long value){
        Item item = new Item(name);
        item.increment(value);
        map.put(name,item);
        return item;
    }

    @Override
    public void incr(String name, long step) {
        lock.lock();
        try {
            Item item = map.get(name);
            if (item == null) {
                newItem(name,step);
                return;
            }
            item.increment(step);
            engine.flush();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized void set(String name, long value) {
        lock.lock();
        try {
            Item item = map.get(name);
            if (item == null) {
                newItem(name,value);
                return;
            }
            item.setValue(value);
            engine.flush();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long incrGet(String name) {
        return incrGet(name,1);
    }

    @Override
    public long incrGet(String name, long step) {
        lock.lock();
        try {
            Item item = map.get(name);
            if (item == null) {
                item = newItem(name,step);
            }else{
                item.increment(step);
            }
            engine.flush();
            return item.getValue();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long get(String name) {
        Item item = map.get(name);
        if (item == null) {
            return 0;
        }
        return item.getValue();
    }

    @Override
    public synchronized long remove(String name) {
        lock.lock();
        try {
            Item item = map.remove(name);
            if (item == null) {
                return 0;
            }
            engine.flush();
            return item.getValue();
        } finally {
            lock.unlock();
        }
    }



    @Override
    public int size() {
        return map.size();
    }

    @Override
    public synchronized void clear() {
        lock.lock();
        try {
            this.map.clear();
            engine.flush();
        } finally {
            lock.unlock();
        }
    }
}
