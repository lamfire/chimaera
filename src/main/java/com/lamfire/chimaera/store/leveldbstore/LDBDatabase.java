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

    DB _db(){
         return manager._db(name);
    }

    @Override
    public byte[] get(byte[] bytes) throws DBException {
        return _db().get(bytes);
    }

    @Override
    public byte[] get(byte[] bytes, ReadOptions readOptions) throws DBException {
        return _db().get(bytes,readOptions);
    }

    @Override
    public DBIterator iterator() {
        return _db().iterator();
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
        return _db().iterator(readOptions);
    }

    @Override
    public void put(byte[] key, byte[] value) throws DBException {
        _db().put(key,value);
    }

    @Override
    public void delete(byte[] key) throws DBException {
        _db().delete(key);
    }

    @Override
    public void write(WriteBatch writeBatch) throws DBException {
        _db().write(writeBatch);
    }

    @Override
    public WriteBatch createWriteBatch() {
        return _db().createWriteBatch();
    }

    @Override
    public Snapshot put(byte[] key, byte[] value, WriteOptions writeOptions) throws DBException {
        return _db().put(key,value,writeOptions);
    }

    @Override
    public Snapshot delete(byte[] key, WriteOptions writeOptions) throws DBException {
        return _db().delete(key, writeOptions);
    }

    @Override
    public Snapshot write(WriteBatch writeBatch, WriteOptions writeOptions) throws DBException {
        return _db().write(writeBatch,writeOptions);
    }

    @Override
    public Snapshot getSnapshot() {
        return _db().getSnapshot();
    }

    @Override
    public long[] getApproximateSizes(Range... ranges) {
        return _db().getApproximateSizes(ranges);
    }

    @Override
    public String getProperty(String name) {
        return _db().getProperty(name);
    }

    @Override
    public void suspendCompactions() throws InterruptedException {
        _db().suspendCompactions();
    }

    @Override
    public void resumeCompactions() {
        _db().resumeCompactions();
    }

    @Override
    public void compactRange(byte[] from, byte[] to) throws DBException {
        _db().compactRange(from,to);
    }

    @Override
    public void close() throws IOException {
        manager.closeDB(name);
    }

    public void clear(){
        manager.deleteDB(name);
    }

    public void delete(){
        manager.deleteDB(name);
    }
}
