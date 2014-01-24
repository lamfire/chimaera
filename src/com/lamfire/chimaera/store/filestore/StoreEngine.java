package com.lamfire.chimaera.store.filestore;

import com.lamfire.utils.Threads;
import org.apache.jdbm.DB;
import org.apache.jdbm.DBMaker;
import org.apache.jdbm.Serialization;
import org.apache.jdbm.Serializer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-12-18
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
public class StoreEngine {
    private String file; //文件路径
    private DB db;

    private int flushThresholdOps = -1;  //最大数据操作缓存次数，当达到该值时，刷新更改到文件。
    private AtomicInteger ops = new AtomicInteger(); //数据更改次数记录器
    private ScheduledExecutorService service;
    private int flushInterval = 15;
    private int cacheSize = -1;

    public StoreEngine(String file,boolean enableLocking,boolean enableTransactions,boolean deleteFilesAfterClose,int flushThresholdOps,int flushInterval,boolean enableCache, int cacheSize) throws IOException {
        this.file = file;
        this.flushThresholdOps = flushThresholdOps;
        this.flushInterval = flushInterval;
        this.cacheSize =  cacheSize;

        DBMaker maker = DBMaker.openFile(file).closeOnExit();
        if(enableCache){
            //maker.enableMRUCache();
            //maker.setMRUCacheSize(cacheSize);
            maker.enableSoftCache();
        }
        if(!enableLocking){
            maker.disableLocking();
        }
        if(!enableTransactions){
            maker.disableTransactions();
        }
        if(deleteFilesAfterClose){
            maker.deleteFilesAfterClose();
        }

        this.db = maker.make();

        if(flushInterval > 0){
            this.service = Executors.newScheduledThreadPool(1, Threads.makeThreadFactory("StoreEngine"));
            this.service.scheduleWithFixedDelay(flushStoreWorker,flushInterval,flushInterval, TimeUnit.SECONDS);
        }
    }

    public synchronized <K, V> Map<K, V> getHashMap(String name) {
        Map<K, V> map = this.db.getHashMap(name);
        if (map == null) {
            map = this.db.createHashMap(name);
        }
        return map;
    }

    public synchronized <K, V> Map<K, V> getHashMap(String name, Serializer<V> serializer) {
        Map<K, V> map = this.db.getHashMap(name);
        if (map == null) {
            map = this.db.createHashMap(name, new Serialization(), serializer);
        }
        return map;
    }

    public synchronized <K, V> Map<K, V> getHashMap(String name, Serializer<K> keySerializer, Serializer<V> valSerializer) {
        Map<K, V> map = this.db.getHashMap(name);
        if (map == null) {
            map = this.db.createHashMap(name, keySerializer, valSerializer);
        }
        return map;
    }

    public synchronized <E> List<E> getLinkedList(String name, Serializer<E> serializer) {
        List<E> list = this.db.getLinkedList(name);
        if (list == null) {
            list = this.db.createLinkedList(name, serializer);
        }
        return list;
    }

    public synchronized <E> Set<E> getHashSet(String name) {
        Set<E> set = this.db.getHashSet(name);
        if (set == null) {
            set = this.db.createHashSet(name);
        }
        return set;
    }

    public synchronized <E> NavigableSet<E> getTreeSet(String name) {
        NavigableSet<E> set = this.db.getTreeSet(name);
        if (set == null) {
            set = this.db.createTreeSet(name);
        }
        return set;
    }

    public synchronized <E> NavigableSet<E> getTreeSet(String name, Comparator<E> comparator, Serializer<E> serializer) {
        NavigableSet<E> set = this.db.getTreeSet(name);
        if (set == null) {
            set = this.db.createTreeSet(name, comparator, serializer);
        }
        return set;
    }

    public void setFlushThresholdOps(int flushThresholdOps) {
        this.flushThresholdOps = flushThresholdOps;
    }

    /**
     * 检查数据更改缓冲次数，如果达到最大值，则写入到文件
     */
    protected synchronized void cacheOrFlush() {
        if (ops.getAndIncrement() >= flushThresholdOps) {
            //Threads.startup(flushStoreWorker);
            flush();
        }
    }

    public synchronized void remove(String name){
        this.db.deleteCollection(name);
        this.db.commit();
    }

    public synchronized void clear(){
        Map<String,Object> collections  = this.db.getCollections();
        if(collections == null){
            return;
        }
        for(String name:collections.keySet()){
            remove(name);
        }
    }

    public int size(){
        Map<String,Object> collections  = this.db.getCollections();
        if(collections == null){
            return 0;
        }
        return collections.size();
    }

    public Set<String> keys(){
        Map<String,Object> collections  = this.db.getCollections();
        if(collections == null){
            return new HashSet<String>();
        }
        return collections.keySet();
    }

    public boolean exists(String key){
        Map<String,Object> collections  = this.db.getCollections();
        if(collections == null){
            return false;
        }
        return collections.containsKey(key);
    }

    public synchronized void flush() {
        try {
            db.commit();
            ops.set(0);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public synchronized void close(){
        if(this.service != null){
            this.service.shutdown();
            this.service = null;
        }
        db.commit();
        db.close();
    }

    Runnable flushStoreWorker = new Runnable() {
        @Override
        public void run() {
            flush();
        }
    };


}
