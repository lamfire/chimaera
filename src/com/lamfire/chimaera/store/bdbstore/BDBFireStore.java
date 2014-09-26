package com.lamfire.chimaera.store.bdbstore;

import com.lamfire.chimaera.store.*;
import com.lamfire.chimaera.store.memstore.MemoryFireRank;
import com.lamfire.utils.Maps;

import java.util.Map;

/**
 * 磁盘文件持久STORE
 * User: lamfire
 * Date: 14-1-3
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class BDBFireStore implements FireStore {
    //key collection caches
    private final Map<String, FireCollection> keyCaches = Maps.newHashMap();

    private String storeName;
    private BDBEngine engine;

    public BDBFireStore(BDBEngine engine, String storeName) {
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
    public long size(String key) {
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
    public long size() {
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
            result = new BDBFireIncrement(this.engine, key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireList getFireList(String key) {
        FireList result = (FireList)keyCaches.get(key);
        if (result == null) {
            result = new BDBFireList(this.engine,key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireMap getFireMap(String key) {
        FireMap result = (FireMap)keyCaches.get(key);
        if (result == null) {
            result = new BDBFireMap(this.engine, key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireQueue getFireQueue(String key) {
        FireQueue result = (FireQueue)keyCaches.get(key);
        if (result == null) {
            result = new BDBFireQueue(this.engine,key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireSet getFireSet(String key) {
        FireSet result = (FireSet)keyCaches.get(key);
        if (result == null) {
            result = new BDBFireSet(this.engine, key);
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireRank getFireRank(String key) {
        FireRank result = (FireRank)keyCaches.get(key);
        if (result == null) {
            result = new MemoryFireRank();
            keyCaches.put(key, result);
        }
        return result;
    }

    @Override
    public void defrag() {

    }

    public void close(){
        this.engine.sync();
        try {
            this.engine.close();
        } catch (BDBStorageException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
