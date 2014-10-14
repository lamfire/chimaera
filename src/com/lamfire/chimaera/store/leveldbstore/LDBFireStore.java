package com.lamfire.chimaera.store.leveldbstore;

import com.lamfire.chimaera.store.*;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Maps;
import org.iq80.leveldb.Options;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-26
 * Time: 下午3:59
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireStore implements FireStore {
    private static final Logger LOGGER = Logger.getLogger(LDBFireStore.class);

    private final Map<String, FireCollection> dbs = Maps.newHashMap();

    private String name;
    private String storageDir;
    private LevelDB levelDB;

    public LDBFireStore(String storageDir,String name){
        LOGGER.info("Make 'LDBFireStore' : " + storageDir + " - " + name);
        this.storageDir = storageDir;
        this.name = name;
        this.levelDB = new LevelDB(storageDir);
        this.levelDB.open();
        init();
    }

    public LDBFireStore(String storageDir,String name,Options options){
        LOGGER.info("Make 'LDBFireStore' : " + storageDir + " - " + name);
        this.storageDir = storageDir;
        this.name = name;
        this.levelDB = new LevelDB(storageDir,options);
        this.levelDB.open();
        init();
    }

    private void init(){
        //读取已经存在的DB
        byte[] prefix = levelDB.asBytes(LevelDB.META_KEY_PREFIX_DATABASE);
        Map<byte[],byte[]> map = levelDB.findMetaByPrefix(prefix);
        for(Map.Entry<byte[] ,byte[]> e : map.entrySet()){
            String dbName = levelDB.decodeDatabaseKey(e.getKey());
            String clsName = levelDB.asString(e.getValue());
            load(dbName,clsName);
        }
    }

    private void load(String name,String className){
        LOGGER.info("Loading data collection [" + name +"] : " + className);
        if ("LDBFireSet".equals(className)){
                getFireSet(name);
        }else if("LDBFireList".equals(className)){
                getFireList(name);
        }else if("LDBFireRank".equals(className)){
            getFireRank(name);
        }else if("LDBFireQueue".equals(className)){
            getFireQueue(name);
        }else if("LDBFireIncrement".equals(className)){
            getFireIncrement(name);
        }else if("LDBFireMap".equals(className)){
            getFireMap(name);
        }
    }

    private void register(String name,FireCollection col){
        String clsName = col.getClass().getSimpleName();
        byte[] dbKey = levelDB.encodeDatabaseKey(name);
        levelDB.setMetaValue(dbKey,levelDB.asBytes(clsName));
        dbs.put(name,col);
    }

    @Override
    public void remove(String key) {
        FireCollection col = dbs.remove(key);
        col.clear();
        this.levelDB.deleteDB(key);
        this.levelDB.removeMeta(levelDB.encodeDatabaseKey(name));
    }

    @Override
    public long size(String key) {
        FireCollection col = dbs.get(key);
        if(col != null){
            return col.size();
        }
        return 0;
    }

    @Override
    public void clear(String key) {
        FireCollection col = dbs.get(key);
        col.clear();
    }

    @Override
    public long size() {
        return dbs.size();
    }

    @Override
    public synchronized void clear() {
       for(Map.Entry<String,FireCollection> e : dbs.entrySet()){
           e.getValue().clear();
       }
       dbs.clear();
       levelDB.clearMeta();
    }

    @Override
    public boolean exists(String key) {
        return dbs.containsKey(key);
    }

    @Override
    public synchronized FireIncrement getFireIncrement(String key) {
        FireIncrement result = (FireIncrement)dbs.get(key);
        if (result == null) {
            result = new LDBFireIncrement(this.levelDB, key);
            register(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireList getFireList(String key) {
        FireList result = (FireList)dbs.get(key);
        if (result == null) {
            result = new LDBFireList(this.levelDB,key);
            register(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireMap getFireMap(String key) {
        FireMap result = (FireMap)dbs.get(key);
        if (result == null) {
            result = new LDBFireMap(this.levelDB, key);
            register(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireQueue getFireQueue(String key) {
        FireQueue result = (FireQueue)dbs.get(key);
        if (result == null) {
            result = new LDBFireQueue(this.levelDB,key);
            register(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireSet getFireSet(String key) {
        FireSet result = (FireSet)dbs.get(key);
        if (result == null) {
            result = new LDBFireSet(this.levelDB, key);
            register(key, result);
        }
        return result;
    }

    @Override
    public synchronized FireRank getFireRank(String key) {
        FireRank result = (FireRank)dbs.get(key);
        if (result == null) {
            result = new LDBFireRank(levelDB,key);
            register(key, result);
        }
        return result;
    }

    @Override
    public void defrag() {

    }
}
