package com.lamfire.chimaera.store.bdbstore;

import com.lamfire.chimaera.store.FireSet;
import com.sleepycat.collections.StoredKeySet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-7-14
 * Time: 下午1:30
 * To change this template use File | Settings | File Templates.
 */
public class BDBFireSet implements FireSet {

    private BDBEngine engine;
    private StoredKeySet<byte[]> set;
    private String name;
    private Incrementer counter;

    private final Lock lock = new ReentrantLock();

    public BDBFireSet(BDBEngine engine ,String name){
        this.engine = engine;
        this.name = name;
        set = engine.getSet(name);
        this.counter = engine.getIncrementor(name + "_COUNTER");
    }


    @Override
    public void add(byte[] value) {
        try{
            lock.lock();
            boolean exists = set.contains(value);
            set.add(value);
            if(!exists){
                counter.increment();
            }
        }finally {
            lock.unlock();
        }

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
    public byte[] remove(byte[] value) {
        try{
            lock.lock();
            boolean exists = set.contains(value);
            set.remove(value);
            if(exists){
                counter.increment(-1);
            }
            return value;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] get(int index) {
        try{
            lock.lock();
            int i=0;
            for(byte[] e:set){
                if(i == index){
                    return e;
                }
                i++;
            }
            return null;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        try{
            lock.lock();
            List<byte[]> list = new ArrayList<byte[]>();
            int i=0;
            for(byte[] e:set){
                if(i >= fromIndex){
                    list.add(e);
                    if(list.size() == size){
                        break;
                    }
                }
                i++;
            }
            return list;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public boolean exists(byte[] bytes) {
        return set.contains(bytes);
    }

    @Override
    public int size() {
        return (int)counter.get();
    }

    @Override
    public void clear() {
        try{
            lock.lock();
            set.clear();
            counter.set(0);
        }finally {
            lock.unlock();
        }
    }
}
