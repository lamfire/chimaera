package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.*;
import com.lamfire.chimaera.store.memstore.FireIncrementInMemory;
import com.lamfire.chimaera.store.memstore.FireRankInMemory;
import com.lamfire.utils.Maps;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-3
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class FileFireStore implements FireStore {
    private static final String KEY_META = "_META_";
    private static final String KEY_INCREMENT = "_INCREMENT_";
    private Map<String, String> meta; //meta info as json
    private Map<String, FireIncrement> increments;
    private Map<String,FireListInFile> fireListCache = Maps.newHashMap();
    private Map<String,FireMapInFile> fireMapCache = Maps.newHashMap();
    private Map<String,FireQueueInFile> fireQueueCache = Maps.newHashMap();
    private Map<String,FireSetInFile> fireSetCache = Maps.newHashMap();
    private Map<String,FireRank> fireRankCache = Maps.newHashMap();

    private String storeName;
    private StoreEngine engine;

    public FileFireStore(String file,String storeName){
        this.storeName = storeName;
        try {
            this.engine = new StoreEngine(file);
            this.meta = this.engine.getHashMap(KEY_META);
            this.increments = this.engine.getHashMap(KEY_INCREMENT, new IncrementSerializer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStoreName(){
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
        if(result == null){
            result =  new FireIncrementInMemory();
            increments.put(key,result);
        }
        return result;
    }

    @Override
    public synchronized FireList getFireList(String key) {
        FireListInFile result = fireListCache.get(key);
        if(result == null){
            result =  new FireListInFile(this.engine,key);
            fireListCache.put(key,result);
        }
        return result;
    }

    @Override
    public synchronized FireMap getFireMap(String key) {
        FireMapInFile result = fireMapCache.get(key);
        if(result == null){
            result =  new FireMapInFile(this.engine,key);
            fireMapCache.put(key,result);
        }
        return result;
    }

    @Override
    public synchronized FireQueue getFireQueue(String key) {
        FireQueueInFile result = fireQueueCache.get(key);
        if(result == null){
            result =  new FireQueueInFile(this.engine,key);
            fireQueueCache.put(key,result);
        }
        return result;
    }

    @Override
    public synchronized FireSet getFireSet(String key) {
        FireSetInFile result = fireSetCache.get(key);
        if(result == null){
            result =  new FireSetInFile(this.engine,key);
            fireSetCache.put(key,result);
        }
        return result;
    }

    @Override
    public FireRank getFireRank(String key) {
        FireRank result = fireRankCache.get(key);
        if(result == null){
            result =  new FireRankInMemory();
            fireRankCache.put(key,result);
        }
        return result;
    }
}
