package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.*;
import com.lamfire.chimaera.store.memstore.MemoryFireRank;
import com.lamfire.utils.Maps;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-26
 * Time: 下午3:59
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireStore implements FireStore {
    private final Map<String, FireCollection> caches = Maps.newHashMap();

    private String name;
    private String storageDir;
    private LevelDB levelDB;

    public LDBFireStore(String storageDir,String name){
        this.storageDir = storageDir;
        this.name = name;
        this.levelDB = new LevelDB(storageDir);
        this.levelDB.open();
    }

    @Override
    public void remove(String key) {
        this.levelDB.deleteDB(key);
    }

    @Override
    public long size(String key) {
        return 0;
    }

    @Override
    public void clear(String key) {
        this.levelDB.deleteDB(key);
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean exists(String key) {
        return false;
    }

    @Override
    public FireIncrement getFireIncrement(String key) {
        FireIncrement result = (FireIncrement)caches.get(key);
        if (result == null) {
            result = new LDBFireIncrement(this.levelDB, key);
            caches.put(key, result);
        }
        return result;
    }

    @Override
    public FireList getFireList(String key) {
        FireList result = (FireList)caches.get(key);
        if (result == null) {
            result = new LDBFireList(this.levelDB,key);
            caches.put(key, result);
        }
        return result;
    }

    @Override
    public FireMap getFireMap(String key) {
        FireMap result = (FireMap)caches.get(key);
        if (result == null) {
            result = new LDBFireMap(this.levelDB, key);
            caches.put(key, result);
        }
        return result;
    }

    @Override
    public FireQueue getFireQueue(String key) {
        FireQueue result = (FireQueue)caches.get(key);
        if (result == null) {
            result = new LDBFireQueue(this.levelDB,key);
            caches.put(key, result);
        }
        return result;
    }

    @Override
    public FireSet getFireSet(String key) {
        FireSet result = (FireSet)caches.get(key);
        if (result == null) {
            result = new LDBFireSet(this.levelDB, key);
            caches.put(key, result);
        }
        return result;
    }

    @Override
    public FireRank getFireRank(String key) {
        FireRank result = (FireRank)caches.get(key);
        if (result == null) {
            result = new LDBFireRank(levelDB,key);
            caches.put(key, result);
        }
        return result;
    }

    @Override
    public void defrag() {

    }
}
