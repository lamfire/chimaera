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
    private final LDBMeta meta;
    private final LDBDatabase _db;
    private final byte[] writeIndexKey;
    private final String name;

    public LDBFixedFireList(LDBMeta meta,LDBDatabase db,String name) {
        this.meta = meta;
        this.name = name;
        this._db =db;
        this.writeIndexKey = meta.getWriteIndexKey(name);
    }

    private synchronized DB getDB() {
        return _db;
    }

    public long getWriteIndex() {
        return meta.getValueAsLong(writeIndexKey);
    }

    void incrementWriteIndex() {
        meta.increment(writeIndexKey);
    }

    @Override
    public boolean add(byte[] value) {
        try {
            lock.lock();
            long index = getWriteIndex();
            getDB().put(Bytes.toBytes(index), value);
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
            getDB().put(Bytes.toBytes(key), value);
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
            return getDB().get(Bytes.toBytes(key));
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
            _db.clear();
            meta.remove(writeIndexKey);
        } finally {
            lock.unlock();
        }
    }
}
