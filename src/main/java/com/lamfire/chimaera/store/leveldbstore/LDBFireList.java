package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.logger.Logger;
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
    private static final Logger LOGGER = Logger.getLogger(LDBFireList.class);
    private final Lock lock = new ReentrantLock();
    private final LDBMeta meta;
    private final LDBDatabase _db;
    private final byte[] writeIndexKey;
    private final byte[] sizeKey;
    private final String name;

    public LDBFireList(LDBMeta meta,LDBDatabase db,String name){
        this.meta = meta;
        this.name = name;
        this._db = db;
        this.writeIndexKey = meta.getWriteIndexKey(name);
        this.sizeKey = meta.getSizeKey(name);
    }

    private synchronized LDBDatabase getDB(){
        return _db;
    }

    public long getWriteIndex(){
        return meta.getValueAsLong(writeIndexKey);
    }

    void incrementWriteIndex(){
        meta.increment(writeIndexKey);
    }

    public long getSize(){
        return meta.getValueAsLong(sizeKey);
    }

    void incrementSize(){
        meta.increment(sizeKey);
    }

    void decrementSize(){
        meta.increment(sizeKey,-1);
    }

    private synchronized Map.Entry<byte[] ,byte[]> getEntry(int index){
        DBIterator it = getDB().iterator();
        try{
            lock.lock();

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
        }finally {
            lock.unlock();
            LDBManager.closeIterator(it);
        }
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

        DBIterator it = getDB().iterator();
        try{
            lock.lock();

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
            LDBManager.closeIterator(it);
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
            _db.clear();
            meta.remove(writeIndexKey);
            meta.remove(sizeKey);
        }finally {
            lock.unlock();
        }
    }
}
