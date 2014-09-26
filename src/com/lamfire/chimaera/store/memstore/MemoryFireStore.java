package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.ChimaeraException;
import com.lamfire.chimaera.store.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryFireStore implements FireStore {

    private final Map<String, Object> store = new ConcurrentHashMap<String, Object>();
    private String storeName;

    public MemoryFireStore(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return this.storeName;
    }

    boolean isInstance(String key, Class<?> type) {
        Object obj = store.get(key);
        return type.isInstance(obj);
    }

    @Override
    public long size() {
        return store.size();
    }

    @Override
    public void clear() {
        store.clear();
    }

    @SuppressWarnings("unchecked")
    private <T> T assertInstance(String key, Class<T> type) {
        Object obj = store.get(key);
        if (obj == null) {
            return null;
        }
        if (type.isInstance(obj)) {
            return (T) obj;
        }
        throw new ChimaeraException(obj.getClass().getName() + " not instance of " + type.getName());
    }

    @Override
    public FireIncrement getFireIncrement(String key) {
        FireIncrement result = assertInstance(key, FireIncrement.class);
        if (result == null) {
            result = new MemoryFireIncrement();
            store.put(key, result);
        }
        return result;
    }

    @Override
    public FireList getFireList(String key) {
        FireList result = assertInstance(key, FireList.class);
        if (result == null) {
            result = new MemoryFireList();
            store.put(key, result);
        }
        return result;
    }

    @Override
    public FireMap getFireMap(String key) {
        FireMap result = assertInstance(key, FireMap.class);
        if (result == null) {
            result = new MemoryFireMap();
            store.put(key, result);
        }
        return result;
    }

    @Override
    public FireQueue getFireQueue(String key) {
        FireQueue result = assertInstance(key, FireQueue.class);
        if (result == null) {
            result = new MemoryFireQueue();
            store.put(key, result);
        }
        return result;
    }

    @Override
    public FireSet getFireSet(String key) {
        FireSet result = assertInstance(key, FireSet.class);
        if (result == null) {
            result = new MemoryFireSet();
            store.put(key, result);
        }
        return result;
    }

    @Override
    public FireRank getFireRank(String key) {
        FireRank result = assertInstance(key, FireRank.class);
        if (result == null) {
            result = new MemoryFireRank();
            store.put(key, result);
        }
        return result;
    }

    @Override
    public void defrag() {
        //数据在内存中,无需整理
    }

    @Override
    public void remove(String key) {
        store.remove(key);
    }

    @Override
    public long size(String key) {
        try {
            FireCollection c = assertInstance(key, FireCollection.class);
            return c.size();
        } catch (Exception e) {
            throw new ChimaeraException("The key[" + key + "] cannot read size,must be instanceof 'FireCollection'");
        }
    }

    @Override
    public void clear(String key) {
        try {
            FireCollection c = assertInstance(key, FireCollection.class);
            c.clear();
        } catch (Exception e) {
            throw new ChimaeraException("The key[" + key + "] cannot read size,must be instanceof 'FireCollection'");
        }
    }

    @Override
    public boolean exists(String key) {
        return store.containsKey(key);
    }

}
