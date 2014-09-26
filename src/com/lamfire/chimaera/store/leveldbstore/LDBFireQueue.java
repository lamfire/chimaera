package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.utils.Bytes;
import org.iq80.leveldb.DB;

import java.util.NoSuchElementException;
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
    private DB db;
    private byte[] writeIndexKey;
    private byte[] readIndexKey;

    private String name;

    public LDBFireQueue(LevelDB levelDB,String name){
        this.levelDB = levelDB;
        this.name = name;
        this.db = levelDB.getDB(name);
        this.readIndexKey = levelDB.encodeReadIndexKey(name);
        this.writeIndexKey = levelDB.encodeWriteIndexKey(name);
    }

    public long getWriteIndex(){
         return levelDB.getMetaValueAsLong(writeIndexKey);
    }

    public long getReadIndex(){
        return levelDB.getMetaValueAsLong(readIndexKey);
    }

    void incrementWriteIndex(){
        levelDB.incrementMeta(writeIndexKey);
    }

    void incrementReadIndex(){
        levelDB.incrementMeta(readIndexKey);
    }

    @Override
    public void push(byte[] value) {
        long index = getWriteIndex();
        db.put(Bytes.toBytes(index),value);
        incrementWriteIndex();
    }

    @Override
    public byte[] pop() {
        byte[] bytes = peek();
        incrementReadIndex();
        return bytes;
    }

    @Override
    public byte[] peek() {
        long writeIndex = getWriteIndex();
        long readIndex = getReadIndex();
        if(readIndex >= writeIndex){
            throw new NoSuchElementException(this.name);
        }

        byte[] bytes = db.get(Bytes.toBytes(readIndex));
        return bytes;
    }

    @Override
    public long size() {
        long writeIndex = getWriteIndex();
        long readIndex = getReadIndex();

        return writeIndex - readIndex;
    }

    @Override
    public void clear() {
        try{
            lock.lock();
            levelDB.deleteDB(name);
            levelDB.removeMeta(writeIndexKey);
            levelDB.removeMeta(readIndexKey);
            this.db = levelDB.getDB(name);
        }finally {
            lock.unlock();
        }
    }
}
