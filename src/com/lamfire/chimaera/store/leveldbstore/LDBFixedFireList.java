package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.hydra.exception.NotSupportedMethodException;
import com.lamfire.utils.Bytes;
import org.iq80.leveldb.DB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class LDBFixedFireList implements FireList {
    private final Lock lock = new ReentrantLock();
    private LevelDB levelDB;
    private DB db;
    private byte[] writeIndexKey;
    private String name;

    public LDBFixedFireList(LevelDB levelDB, String name) {
        this.levelDB = levelDB;
        this.name = name;
        this.db = levelDB.getDB(name);
        this.writeIndexKey = levelDB.encodeWriteIndexKey(name);
    }

    private synchronized DB getDB() {
        if (this.db == null) {
            db = levelDB.getDB(name);
        }
        return db;
    }

    public long getWriteIndex() {
        return levelDB.getMetaValueAsLong(writeIndexKey);
    }

    void incrementWriteIndex() {
        levelDB.incrementMeta(writeIndexKey);
    }

    @Override
    public boolean add(byte[] value) {
        try {
            lock.lock();
            long index = getWriteIndex();
            db.put(Bytes.toBytes(index), value);
            incrementWriteIndex();
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void set(int index, byte[] value) {
        try {
            lock.lock();
            long size = getWriteIndex();
            if (index >= size) {
                throw new IndexOutOfBoundsException("Index " + index + ",Size " + size);
            }
            long key = (long) index;
            db.put(Bytes.toBytes(key), value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] get(int index) {
        try {
            lock.lock();
            long size = getWriteIndex();
            if (index >= size) {
                throw new IndexOutOfBoundsException("Index " + index + ",Size " + size);
            }
            long key = (long) index;
            return db.get(Bytes.toBytes(key));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        long max = getWriteIndex();
        if (fromIndex + size >= max) {
            throw new IndexOutOfBoundsException("Index [" + fromIndex + " - " + (fromIndex + size) + "],Size " + max);
        }
        List<byte[]> list = new ArrayList<byte[]>(size);
        try {
            lock.lock();
            long maxIndex = getWriteIndex();
            for (int i = fromIndex; i < maxIndex; i++) {
                list.add(get(i));
                if (list.size() >= size) {
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
        return list;
    }

    @Override
    public byte[] remove(int index) {
        throw new NotSupportedMethodException("cannot remove elements");
    }

    @Override
    public long size() {
        return getWriteIndex();
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            levelDB.deleteDB(name);
            levelDB.removeMeta(writeIndexKey);
            this.db = levelDB.getDB(name);
        } finally {
            lock.unlock();
        }
    }
}
