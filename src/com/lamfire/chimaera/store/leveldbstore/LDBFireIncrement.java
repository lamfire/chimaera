package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.utils.Bytes;
import org.iq80.leveldb.DB;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午3:39
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireIncrement implements FireIncrement {
    private final Lock lock = new ReentrantLock();
    private LevelDB levelDB;
    private DB db;
    private byte[] sizeKey;
    private String name;

    public LDBFireIncrement(LevelDB levelDB, String name) {
        this.levelDB = levelDB;
        this.name = name;
        db = levelDB.getDB(name);
        this.sizeKey = levelDB.encodeSizeKey(name);
    }

    private void incrSize() {
        levelDB.incrementMeta(this.sizeKey);
    }

    private void decrSize() {
        levelDB.incrementMeta(this.sizeKey,-1);
    }

    byte[] asBytes(String message) {
        return levelDB.bytes(message);
    }

    @Override
    public void incr(String name) {
        incr(name, 1);
    }

    @Override
    public void incr(String name, long step) {
        incrGet(name,step);
    }

    @Override
    public long get(String name) {
        try {
            lock.lock();
            byte[] key = asBytes(name);
            long value = 0;
            byte[] bytes = db.get(key);
            if (bytes != null) {
                value = Bytes.toLong(bytes);
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void set(String name, long value) {
        try {
            lock.lock();
            byte[] key = asBytes(name);
            byte[] bytes = db.get(key);
            if (bytes == null) {
                incrSize();
            }
            db.put(key, Bytes.toBytes(value));
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
        try {
            lock.lock();
            byte[] key = asBytes(name);
            long value = 0;
            byte[] bytes = db.get(key);
            if (bytes != null) {
                value = Bytes.toLong(bytes);
            } else {
                incrSize();
            }
            value += step;
            db.put(key, Bytes.toBytes(value));
            return value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long remove(String name) {
        try {
            lock.lock();
            byte[] key = asBytes(name);
            long value = 0;
            byte[] bytes = db.get(key);
            if (bytes != null) {
                db.delete(key);
                decrSize();
                value = Bytes.toLong(bytes);
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long size() {
        try {
            lock.lock();
            return levelDB.getMetaValueAsLong(this.sizeKey);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            levelDB.deleteDB(name);
            levelDB.removeMeta(sizeKey);
            this.db = levelDB.getDB(name);
        } finally {
            lock.unlock();
        }
    }
}
