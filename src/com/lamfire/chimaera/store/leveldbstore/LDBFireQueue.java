package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.utils.Bytes;
import org.iq80.leveldb.DB;

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
    private LevelDB levelDB;
    private DB _db;
    private byte[] writeIndexKey;
    private byte[] readIndexKey;

    private AtomicLong writeAtome;
    private AtomicLong readAtome;

    private String name;

    public LDBFireQueue(LevelDB levelDB, String name) {
        this.levelDB = levelDB;
        this.name = name;
        this._db = levelDB.getDB(name);
        this.readIndexKey = levelDB.encodeReadIndexKey(name);
        this.writeIndexKey = levelDB.encodeWriteIndexKey(name);

        long writeIndex = levelDB.getMetaValueAsLong(writeIndexKey);
        this.writeAtome = new AtomicLong(writeIndex);

        long readIndex =  levelDB.getMetaValueAsLong(readIndexKey);
        this.readAtome = new AtomicLong(readIndex);
    }

    private synchronized DB getDB() {
        if (this._db == null) {
            _db = levelDB.getDB(name);
        }
        return _db;
    }

    public long getWriteIndex() {
        return writeAtome.get();
    }

    public long getReadIndex() {
        return readAtome.get();
    }

    void incrementWriteIndex() {
        long val = this.writeAtome.incrementAndGet();
        levelDB.setMetaValue(writeIndexKey,Bytes.toBytes(val));
    }

    void incrementReadIndex() {
        long val = this.readAtome.incrementAndGet();
        levelDB.setMetaValue(readIndexKey,Bytes.toBytes(val));
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
            levelDB.deleteDB(name);
            levelDB.removeMeta(writeIndexKey);
            levelDB.removeMeta(readIndexKey);
            this._db = null;
        } finally {
            lock.unlock();
        }
    }
}
