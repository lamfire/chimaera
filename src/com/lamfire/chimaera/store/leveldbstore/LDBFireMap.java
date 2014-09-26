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
    private LevelDB levelDB;
    private DB db;
    private byte[] sizeKey;
    private String name;

    public LDBFireMap(LevelDB levelDB,String name){
        this.levelDB = levelDB;
        this.name = name;
        db = levelDB.getDB(name);
        this.sizeKey = levelDB.encodeSizeKey(name);
    }

    private void incrSize(long incr){
        levelDB.incrementMeta(this.sizeKey,incr);
    }

    @Override
    public void put(String key, byte[] value) {
        try{
            lock.lock();
            byte[] keyBytes =levelDB.bytes(key);
            byte[] oldValue = db.get(keyBytes);

            if(!value.equals(oldValue)){
                db.put(levelDB.bytes(key),value);
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
        try{
            lock.lock();
            List<String> keys = Lists.newArrayList();
            DBIterator it =  db.iterator();
            it.seekToFirst();
            while(it.hasNext()){
                byte[] keyBytes = it.next().getKey();
                keys.add(levelDB.asString(keyBytes));
            }
            return keys;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] get(String key) {
        try{
            lock.lock();
            return db.get(levelDB.bytes(key));
        }finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized void remove(String key) {
        try{
            lock.lock();
            byte[] keyBytes =levelDB.bytes(key);
            byte[] oldValue = db.get(keyBytes);
            db.delete(keyBytes);
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
            return levelDB.getMetaValueAsLong(this.sizeKey);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try{
            lock.lock();
            levelDB.deleteDB(name);
            levelDB.removeMeta(sizeKey);
            this.db = levelDB.getDB(name);
        }finally {
            lock.unlock();
        }
    }
}
