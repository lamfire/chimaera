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
    private final LDBMeta meta;
    private final LDBDatabase _db;
    private final byte[] sizeKey;
    private final String name;

    public LDBFireIncrement(LDBMeta meta,LDBDatabase db,String name) {
        this.meta = meta;
        this.name = name;
        this._db = db;
        this.sizeKey = meta.getSizeKey(name);
    }

    private synchronized LDBDatabase getDB(){
        return _db;
    }

    private void incrSize() {
        meta.increment(this.sizeKey);
    }

    private void decrSize() {
        meta.increment(this.sizeKey,-1);
    }

    byte[] asBytes(String message) {
        return LDBManager.asBytes(message);
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
            byte[] bytes = getDB().get(key);
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
            byte[] bytes = getDB().get(key);
            if (bytes == null) {
                incrSize();
            }
            getDB().put(key, Bytes.toBytes(value));
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
            byte[] bytes = getDB().get(key);
            if (bytes != null) {
                value = Bytes.toLong(bytes);
            } else {
                incrSize();
            }
            value += step;
            getDB().put(key, Bytes.toBytes(value));
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
            byte[] bytes = getDB().get(key);
            if (bytes != null) {
                getDB().delete(key);
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
            return meta.getValueAsLong(this.sizeKey);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            getDB().clear();
            meta.remove(sizeKey);
        } finally {
            lock.unlock();
        }
    }
}
