package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.utils.Lists;

import java.util.*;

/**
 * 持久化的FireMap类，该对象中的数据将会被持久化到文件中。
 */
public class DiskFireMap implements FireMap {
    private StoreEngine engine;
    private String name;
    private Map<String,byte[]> map;
    public DiskFireMap(StoreEngine engine, String name){
        this.engine = engine;
        this.name = name;
        this.map = engine.getHashMap(this.name);
    }

	@Override
	public void put(String key, byte[] value)  {
		map.put(key, value);
        engine.cacheOrFlush();
    }

    @Override
    public List<String> keys() {
        List<String> list = Lists.newArrayList(map.keySet());
        return list;
    }

    public Set<String> getKeys(){
        return map.keySet();
    }

    public Collection<byte[]> getValues(){
        return map.values();
    }

	@Override
	public byte[] get(String key) {
		return map.get(key);
	}

	
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public void remove(String key) {
		map.remove(key);
        engine.cacheOrFlush();
	}

    @Override
    public boolean exists(String key) {
        return map.containsKey(key);
    }

    @Override
	public synchronized void clear() {
		map.clear();
        engine.cacheOrFlush();
	}

}
