package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.*;
import com.lamfire.chimaera.store.memstore.MemoryFireIncrement;
import com.lamfire.utils.Maps;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-3
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireStore implements FireStore {
    private static final String KEY_META = "_META_";
    private static final String KEY_INCREMENT = "_INCREMENT_";
    private Map<String, String> meta; //meta info as json
    private Map<String, FireIncrement> increments;
    private Map<String, DiskFireList> fireListCache = Maps.newHashMap();
    private Map<String, DiskFireMap> fireMapCache = Maps.newHashMap();
    private Map<String, DiskFireQueue> fireQueueCache = Maps.newHashMap();
    private Map<String, DiskFireSet> fireSetCache = Maps.newHashMap();
    private Map<String, FireRank> fireRankCache = Maps.newHashMap();

    private String storeName;
    private StoreEngine engine;

    public DiskFireStore(String file, String storeName) {
        this.storeName = storeName;
        try {
            this.engine = new StoreEngine(file);
            this.meta = this.engine.getHashMap(KEY_META);
            this.increments = this.engine.getHashMap(KEY_INCREMENT, new IncrementSerializer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStoreName() {
        return this.storeName;
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public int size(String key) {
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clear(String key) {

    }

    @Override
    public int size() {
        return this.meta.size();
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean exists(String key) {
        return this.meta.containsKey(key);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public synchronized FireIncrement getFireIncrement(String key) {
        FireIncrement result = increments.get(key);
        if (result == null) {
            result = new MemoryFireIncrement();
            increments.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireList getFireList(String key) {
        DiskFireList result = fireListCache.get(key);
        if (result == null) {
            result = new DiskFireList(this.engine, key);
            fireListCache.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireMap getFireMap(String key) {
        DiskFireMap result = fireMapCache.get(key);
        if (result == null) {
            result = new DiskFireMap(this.engine, key);
            fireMapCache.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireQueue getFireQueue(String key) {
        DiskFireQueue result = fireQueueCache.get(key);
        if (result == null) {
            result = new DiskFireQueue(this.engine, key);
            fireQueueCache.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireSet getFireSet(String key) {
        DiskFireSet result = fireSetCache.get(key);
        if (result == null) {
            result = new DiskFireSet(this.engine, key);
            fireSetCache.put(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireRank getFireRank(String key) {
        FireRank result = fireRankCache.get(key);
        if (result == null) {
            result = new DiskFireRank(this.engine, key);
            fireRankCache.put(key, result);
        }
        return result;
    }
}
