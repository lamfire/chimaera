package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.utils.Lists;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-23
 * Time: 下午5:54
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireMap implements FireMap {
    private final Lock lock = new ReentrantLock();
    private final LDBMeta meta;
    private final LDBDatabase _db;
    private final byte[] sizeKey;
    private final String name;

    public LDBFireMap(LDBMeta meta,LDBDatabase db,String name){
        this.meta = meta;
        this.name = name;
        this._db = db;
        this.sizeKey = meta.getSizeKey(name);
    }

    private synchronized LDBDatabase getDB(){
        return _db;
    }

    private void incrSize(long incr){
        meta.increment(this.sizeKey,incr);
    }

    byte[] asBytes(String message) {
        return LDBManager.asBytes(message);
    }

    String asString(byte[] message) {
        return LDBManager.asString(message);
    }

    @Override
    public void put(String key, byte[] value) {
        try{
            lock.lock();
            byte[] keyBytes =asBytes(key);
            byte[] oldValue = getDB().get(keyBytes);

            if(!value.equals(oldValue)){
                getDB().put(asBytes(key),value);
            }

            if(oldValue == null && value != null){
                incrSize(1);
            }
        }finally {
            lock.unlock();
        }
    }

    @Override
    public List<String> keys() {
        DBIterator it =  getDB().iterator();
        try{
            lock.lock();
            List<String> keys = Lists.newArrayList();

            it.seekToFirst();
            while(it.hasNext()){
                byte[] keyBytes = it.next().getKey();
                keys.add(asString(keyBytes));
            }
            return keys;
        }finally {
            lock.unlock();
            LDBManager.closeIterator(it);
        }
    }

    @Override
    public byte[] get(String key) {
        try{
            lock.lock();
            return getDB().get(asBytes(key));
        }finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized void remove(String key) {
        try{
            lock.lock();
            byte[] keyBytes =asBytes(key);
            byte[] oldValue = getDB().get(keyBytes);
            getDB().delete(keyBytes);
            if(oldValue != null){
                incrSize(-1);
            }
        }finally {
            lock.unlock();
        }
    }

    @Override
    public boolean exists(String key) {
        try{
            lock.lock();
            return get(key)!=null;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public long size() {
        try{
            lock.lock();
            return meta.getValueAsLong(this.sizeKey);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try{
            lock.lock();
            meta.remove(sizeKey);
            _db.clear();
        }finally {
            lock.unlock();
        }
    }
}
