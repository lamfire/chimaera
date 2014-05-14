package com.lamfire.chimaera.store.dbmstore;

import com.lamfire.chimaera.store.*;
import com.lamfire.utils.Maps;

import java.util.Map;

/**
 * 磁盘文件持久STORE
 * User: lamfire
 * Date: 14-1-3
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class DBMFireStore implements FireStore {
    //key collection caches
    private final Map<String, FireCollection> keyCaches = Maps.newHashMap();

    private String storeName;
    private JDBMEngine engine;

    public DBMFireStore(JDBMEngine engine, String storeName) {
        this.storeName = storeName;
        this.engine = engine;
    }

    public String getStoreName() {
        return this.storeName;
    }

    @Override
    public void remove(String key) {
        FireCollection col = keyCaches.get(key);
        if(col != null){
            col.clear();
            keyCaches.remove(key);
        }
    }

    @Override
    public int size(String key) {
        FireCollection col = keyCaches.get(key);
        if(col != null){
            return col.size();
        }
        return 0;
    }

    @Override
    public void clear(String key) {
        FireCollection col = keyCaches.get(key);
        if(col != null){
            col.clear();
        }
    }

    @Override
    public int size() {
        return this.keyCaches.size();
    }

    @Override
    public synchronized void clear() {
        //not supported
    }

    @Override
    public boolean exists(String key) {
        return this.engine.exists(key);
    }

    @Override
    public synchronized FireIncrement getFireIncrement(String key) {
        FireIncrement result = (FireIncrement)keyCaches.get(key);
        if (result == null) {
            result = new DBMFireIncrement(this.engine, key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireList getFireList(String key) {
        DBMFireList result = (DBMFireList)keyCaches.get(key);
        if (result == null) {
            result = new DBMFireList(this.engine, key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireMap getFireMap(String key) {
        DBMFireMap result = (DBMFireMap)keyCaches.get(key);
        if (result == null) {
            result = new DBMFireMap(this.engine, key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireQueue getFireQueue(String key) {
        DBMFireQueue result = (DBMFireQueue)keyCaches.get(key);
        if (result == null) {
            result = new DBMFireQueue(this.engine, key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireSet getFireSet(String key) {
        DBMFireSet result = (DBMFireSet)keyCaches.get(key);
        if (result == null) {
            result = new DBMFireSet(this.engine, key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireRank getFireRank(String key) {
        FireRank result = (FireRank)keyCaches.get(key);
        if (result == null) {
            result = new DBMFireRank(this.engine, key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public void defrag() {
        this.engine.defrag();
    }

    public void close(){
        this.engine.flush();
        this.engine.close();
    }
}
