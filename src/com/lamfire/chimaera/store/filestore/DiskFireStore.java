package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.*;
import com.lamfire.chimaera.store.memstore.MemoryFireIncrement;
import com.lamfire.utils.Maps;
import com.lamfire.utils.StringUtils;

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
    private static final String KEY_INCREMENT = "_INCREMENT_";
    private Map<String, FireIncrement> increments;
    private Map<String, DiskFireList> fireListCache = Maps.newHashMap();
    private Map<String, DiskFireMap> fireMapCache = Maps.newHashMap();
    private Map<String, DiskFireQueue> fireQueueCache = Maps.newHashMap();
    private Map<String, DiskFireSet> fireSetCache = Maps.newHashMap();
    private Map<String, FireRank> fireRankCache = Maps.newHashMap();

    private String storeName;
    private StoreEngine engine;

    public DiskFireStore(String file, String storeName,boolean enableLocking,boolean enableTransactions,boolean deleteFilesAfterClose,int flushThresholdOps,int flushInterval, int cacheSize) {
        this.storeName = storeName;
        try {
            this.engine = new StoreEngine(file,enableLocking,enableTransactions,deleteFilesAfterClose,flushThresholdOps,flushInterval,cacheSize);
            this.increments = this.engine.getHashMap(KEY_INCREMENT, new IncrementSerializer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DiskFireStore(StoreEngine engine,String storeName) {
        this.storeName = storeName;
        this.engine = engine;
        this.increments = this.engine.getHashMap(KEY_INCREMENT, new IncrementSerializer());
    }

    public String getStoreName() {
        return this.storeName;
    }

    @Override
    public void remove(String key) {
         //no supported
    }

    @Override
    public int size(String key) {
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clear(String key) {
        remove(key);
    }

    @Override
    public int size() {
        return -1;
    }

    @Override
    public synchronized void clear() {
        for(String key:this.engine.keys()){
            if(!(StringUtils.equals(key,KEY_INCREMENT))){
                this.engine.remove(key);
            }
        }
        this.increments.clear();
    }

    @Override
    public boolean exists(String key) {
        return this.engine.exists(key);
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
