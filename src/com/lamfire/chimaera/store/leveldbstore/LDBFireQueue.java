package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.utils.Bytes;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 上午9:56
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireQueue implements FireQueue {
    private final Lock lock = new ReentrantLock();
    private final LDBMeta meta;
    private final LDBDatabase _db;
    private final byte[] writeIndexKey;
    private final byte[] readIndexKey;

    private final AtomicLong writeCounter;
    private final AtomicLong readCounter;

    private final String name;

    public LDBFireQueue(LDBMeta meta,LDBDatabase db,String name) {
        this.meta = meta;
        this.name = name;
        this._db = db;
        this.readIndexKey = meta.getReadIndexKey(name);
        this.writeIndexKey = meta.getWriteIndexKey(name);

        long writeIndex = meta.getValueAsLong(writeIndexKey);
        this.writeCounter = new AtomicLong(writeIndex);

        long readIndex =  meta.getValueAsLong(readIndexKey);
        this.readCounter = new AtomicLong(readIndex);
    }

    private synchronized LDBDatabase getDB() {
        return _db;
    }

    public long getWriteIndex() {
        return writeCounter.get();
    }

    public long getReadIndex() {
        return readCounter.get();
    }

    void incrementWriteIndex() {
        long val = this.writeCounter.incrementAndGet();
        meta.setValue(writeIndexKey, Bytes.toBytes(val));
    }

    void incrementReadIndex() {
        long val = this.readCounter.incrementAndGet();
        meta.setValue(readIndexKey, Bytes.toBytes(val));
    }

    @Override
    public void push(byte[] value) {
        try {
            lock.lock();
            long index = getWriteIndex();
            getDB().put(Bytes.toBytes(index), value);
            incrementWriteIndex();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] pop() {
        try {
            lock.lock();
            byte[] bytes = peek();
            incrementReadIndex();
            return bytes;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] peek() {
        try {
            lock.lock();
            long writeIndex = getWriteIndex();
            long readIndex = getReadIndex();
            if (readIndex >= writeIndex) {
                throw new NoSuchElementException(this.name);
            }

            byte[] bytes = getDB().get(Bytes.toBytes(readIndex));
            return bytes;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long size() {
        try {
            lock.lock();
            long writeIndex = getWriteIndex();
            long readIndex = getReadIndex();
            return writeIndex - readIndex;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            _db.clear();
            meta.remove(writeIndexKey);
            meta.remove(readIndexKey);
        } finally {
            lock.unlock();
        }
    }
}
