package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.hydra.exception.NotSupportedMethodException;
import com.lamfire.utils.Bytes;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireList implements FireList{
    private final Lock lock = new ReentrantLock();
    private LevelDB levelDB;
    private DB _db;
    private byte[] writeIndexKey;
    private byte[] sizeKey;
    private String name;

    public LDBFireList(LevelDB levelDB,String name){
        this.levelDB = levelDB;
        this.name = name;
        this._db = levelDB.getDB(name);
        this.writeIndexKey = levelDB.encodeWriteIndexKey(name);
        this.sizeKey = levelDB.encodeSizeKey(name);
    }

    private synchronized DB getDB(){
        if(this._db == null){
            _db = levelDB.getDB(name);
        }
        return _db;
    }

    public long getWriteIndex(){
        return levelDB.getMetaValueAsLong(writeIndexKey);
    }

    void incrementWriteIndex(){
        levelDB.incrementMeta(writeIndexKey);
    }

    public long getSize(){
        return levelDB.getMetaValueAsLong(sizeKey);
    }

    void incrementSize(){
        levelDB.incrementMeta(sizeKey);
    }

    void decrementSize(){
        levelDB.incrementMeta(sizeKey,-1);
    }

    private synchronized Map.Entry<byte[] ,byte[]> getEntry(int index){
        DBIterator it = getDB().iterator();
        it.seekToFirst();
        int count = 0;
        while(it.hasNext()){
            if(count == index){
                break;
            }
            it.next();
            count ++;
        }
        Map.Entry<byte[] ,byte[]> entry = it.next();
        return entry;
    }

    @Override
    public boolean add(byte[] value) {
        try{
            lock.lock();
            long index = getWriteIndex();
            getDB().put(Bytes.toBytes(index),value);
            incrementWriteIndex();
            incrementSize();
            return true;
        }finally {
            lock.unlock();
        }
    }



    @Override
    public void set(int index, byte[] value) {
        try{
            lock.lock();
            long size = getSize();
            if(index >= size){
                throw new IndexOutOfBoundsException("Index " + index +",Size " + size);
            }
            Map.Entry<byte[] ,byte[]> entry = getEntry(index);
            getDB().put(entry.getKey(),value);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public byte[] get(int index) {
        long size = getSize();
        if(index >= size){
            throw new IndexOutOfBoundsException("Index " + index +",Size " + size);
        }
        return getEntry(index).getValue();
    }

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        long max = getSize();
        if(fromIndex + size >= max){
            throw new IndexOutOfBoundsException("Index [" + fromIndex +" - " +(fromIndex + size) +"],Size " + max);
        }
        List<byte[]> list = new ArrayList<byte[]>(size);
        try{
            lock.lock();
            DBIterator it = getDB().iterator();
            it.seekToFirst();
            int count = 0;
            while(it.hasNext()){
                if(count == fromIndex){
                    break;
                }
                it.next();
                count ++;
            }

            while(it.hasNext()){
                Map.Entry<byte[] ,byte[]> entry = it.next();
                list.add(entry.getValue());
                if(list.size() == size){
                    break;
                }
            }

        }finally {
            lock.unlock();
        }
        return list;
    }

    @Override
    public byte[] remove(int index) {
        try{
            lock.lock();
            long size = getSize();
            if(index >= size){
                throw new IndexOutOfBoundsException("Index " + index +",Size " + size);
            }
            Map.Entry<byte[] ,byte[]> entry = getEntry(index);
            getDB().delete(entry.getKey());
            decrementSize();
            return entry.getValue();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public long size() {
        return getSize();
    }

    @Override
    public void clear() {
        try{
            lock.lock();
            levelDB.deleteDB(name);
            levelDB.removeMeta(writeIndexKey);
            levelDB.removeMeta(sizeKey);
            this._db = null;
        }finally {
            lock.unlock();
        }
    }
}
