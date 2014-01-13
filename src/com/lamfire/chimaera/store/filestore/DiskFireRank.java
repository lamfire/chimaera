package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.Item;
import com.lamfire.chimaera.store.ItemComparator;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 上午11:53
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireRank implements FireRank {
    private final Lock lock = new ReentrantLock();
    private NavigableSet<Item> tree;
    private NavigableSet<Item> descending;
    private Map<String,Item> map;
    private StoreEngine engine;
    private String name;

    public DiskFireRank(StoreEngine engine, String name){
        this.engine = engine;
        this.name = name;
        this.map = engine.getHashMap(name+"_MAP",new ItemSerializer());
        this.tree = engine.getTreeSet(name+"_TREE_ASC",new ItemComparator(),new ItemSerializer());
        this.descending = engine.getTreeSet(name+"_TREE_DESC",new ItemComparator().descending(),new ItemSerializer());
    }

    @Override
    public synchronized void put(String name) {
        incr(name,1);
    }

    private synchronized void insert(Item item){
        descending.add(item);
        map.put(item.getName(), item);
        tree.add(item);
    }

    private synchronized void removeOnTree(Item item){
        tree.remove(item);
        descending.remove(item);
    }

    @Override
    public void incr(String name, long step) {
        lock.lock();
        try{
            Item item = map.get(name);
            if(item == null){
                item = new Item(name);
                item.increment(step);
                insert(item);
                return;
            }
            removeOnTree(item);
            item.increment(step);
            insert(item);
            engine.cacheOrFlush();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized void set(String name, long count) {
        lock.lock();
        try{
            Item item = map.get(name);
            if(item == null){
                item = new Item();
                item.setName(name);
                item.setValue(count);
                insert(item);
                return;
            }
            removeOnTree(item);
            item.setValue(count);
            insert(item);
            engine.cacheOrFlush();
        }finally {
            lock.unlock();
        }
    }

    public Iterator<Item> iterator(){
        return tree.iterator();
    }

    @Override
    public long score(String name) {
        Item item = map.get(name);
        if(item == null){
            return 0;
        }
        return item.getValue();
    }

    @Override
    public synchronized long remove(String name) {
        lock.unlock();
        try{
            Item item = map.remove(name);
            if(item == null){
                return 0;
            }
            tree.remove(item);
            descending.remove(item);
            engine.cacheOrFlush();
            return item.getValue();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public List<Item> max(int size) {
        if(tree.isEmpty()){
            return null;
        }
        return subList(tree.iterator(),0, size);
    }

    private synchronized List<Item> subList(Iterator<Item> it,int from,int size){
        lock.lock();
        try{
            List<Item> result = new ArrayList<Item>();
            int count = 0;
            int index = 0;
            while(it.hasNext()){
                Item item = it.next();
                if(index >= from){
                    result.add(item);
                    count ++;
                    if(count >= size){
                        break;
                    }
                }
                index ++;
            }
            return result;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public  List<Item> min(int size) {
        if(tree.isEmpty()){
            return null;
        }
        return subList(descending.iterator(),0,size);
    }

    @Override
    public List<Item> maxRange(int from, int size) {
        if(tree.isEmpty()){
            return null;
        }
        return subList(tree.iterator(),from,size);
    }

    @Override
    public List<Item> minRange(int from, int size) {
        if(tree.isEmpty()){
            return null;
        }
        return subList(descending.iterator(),from,size);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public synchronized void clear() {
        lock.lock();
        try{
            this.tree.clear();
            this.map.clear();
            this.descending.clear();
            engine.cacheOrFlush();
        }finally {
            lock.unlock();
        }
    }
}
