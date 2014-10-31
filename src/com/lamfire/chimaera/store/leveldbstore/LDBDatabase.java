package com.lamfire.chimaera.store.leveldbstore;

import org.iq80.leveldb.*;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-10-29
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class LDBDatabase implements DB {
    private static final com.lamfire.logger.Logger LOGGER = com.lamfire.logger.Logger.getLogger(LDBDatabase.class);
    private final LDBManager manager;
    private final String name;

    LDBDatabase(LDBManager manager,String name){
        this.manager = manager;
        this.name = name;
    }

    private DB getDB(){
         return manager.borrowDB(name);
    }

    @Override
    public byte[] get(byte[] bytes) throws DBException {
        return getDB().get(bytes);
    }

    @Override
    public byte[] get(byte[] bytes, ReadOptions readOptions) throws DBException {
        return getDB().get(bytes,readOptions);
    }

    @Override
    public DBIterator iterator() {
        return getDB().iterator();
    }

    public void closeIterator(DBIterator it){
        try {
            if(it != null)it.close();
        } catch (IOException e) {
            LOGGER.warn(e);
        }
    }

    @Override
    public DBIterator iterator(ReadOptions readOptions) {
        return getDB().iterator(readOptions);
    }

    @Override
    public void put(byte[] key, byte[] value) throws DBException {
        getDB().put(key,value);
    }

    @Override
    public void delete(byte[] key) throws DBException {
        getDB().delete(key);
    }

    @Override
    public void write(WriteBatch writeBatch) throws DBException {
        getDB().write(writeBatch);
    }

    @Override
    public WriteBatch createWriteBatch() {
        return getDB().createWriteBatch();
    }

    @Override
    public Snapshot put(byte[] key, byte[] value, WriteOptions writeOptions) throws DBException {
        return getDB().put(key,value,writeOptions);
    }

    @Override
    public Snapshot delete(byte[] key, WriteOptions writeOptions) throws DBException {
        return getDB().delete(key, writeOptions);
    }

    @Override
    public Snapshot write(WriteBatch writeBatch, WriteOptions writeOptions) throws DBException {
        return getDB().write(writeBatch,writeOptions);
    }

    @Override
    public Snapshot getSnapshot() {
        return getDB().getSnapshot();
    }

    @Override
    public long[] getApproximateSizes(Range... ranges) {
        return getDB().getApproximateSizes(ranges);
    }

    @Override
    public String getProperty(String name) {
        return getDB().getProperty(name);
    }

    @Override
    public void suspendCompactions() throws InterruptedException {
        getDB().suspendCompactions();
    }

    @Override
    public void resumeCompactions() {
        getDB().resumeCompactions();
    }

    @Override
    public void compactRange(byte[] from, byte[] to) throws DBException {
        getDB().compactRange(from,to);
    }

    @Override
    public void close() throws IOException {
        manager.closeDB(name);
    }

    public void clear(){
        manager.deleteDB(name);
    }
}
