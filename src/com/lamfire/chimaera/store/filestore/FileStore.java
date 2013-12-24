package com.lamfire.chimaera.store.filestore;

import org.apache.jdbm.DB;
import org.apache.jdbm.DBMaker;
import org.apache.jdbm.Serializer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-12-18
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
public class FileStore {
    private String file; //文件路径
    private DB db;

    private int maxCacheSize = 100;  //最大数据操作缓存次数，当达到该值时，刷新更改到文件。
    private AtomicInteger cacheCount = new AtomicInteger(); //数据更改次数记录器

    public FileStore(String file)throws IOException{
        this.file = file;
        this.db = DBMaker.openFile(file) .make();
    }

    public DB getDB(){
        return this.db;
    }

    public synchronized  <K,V> Map<K,V> getHashMap(String name){
        Map<K,V> map =  this.db.getHashMap(name);
        if(map == null){
            map = this.db.createHashMap(name);
        }
        return map;
    }

    public synchronized <E> List<E> getLinkedList(String name){
        List<E> list = this.db.getLinkedList(name);
        if(list == null){
            list = this.db.createLinkedList(name);
        }
        return list;
    }

    public synchronized <E>  Set<E> getHashSet(String name){
        Set<E> set = this.db.getHashSet(name);
        if(set == null){
            set = this.db.createHashSet(name);
        }
        return set;
    }

    public synchronized <E> Set<E> getTreeSet(String name){
        Set<E> set = this.db.getTreeSet(name);
        if(set == null){
            set = this.db.createTreeSet(name);
        }
        return set;
    }

    public void setMaxCacheSize(int maxCacheSize){
        this.maxCacheSize = maxCacheSize;
    }

    /**
     * 检查数据更改缓冲次数，如果达到最大值，则写入到文件
     */
    protected synchronized void cacheOrFlush(){
        if(cacheCount.getAndIncrement() >= maxCacheSize){
            flush();
        }
    }

    public synchronized void flush(){
        try {
            db.commit();
            cacheCount.set(0);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}
