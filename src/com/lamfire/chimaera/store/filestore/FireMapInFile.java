package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.utils.Lists;

import java.io.IOException;
import java.util.*;

/**
 * 持久化的FireMap类，该对象中的数据将会被持久化到文件中。
 */
public class FireMapInFile extends FileStore implements FireMap {
    private String storeKey;
    private Map<String,byte[]> map;
    public FireMapInFile(String file,String storeKey)throws IOException{
        super(file);
        this.storeKey = storeKey;
        this.map = getDB().getHashMap(this.storeKey);
        if(this.map == null){
            this.map = getDB().createHashMap(this.storeKey) ;
        }
        super.setMaxCacheSize(-1);
    }

    public FireMapInFile(String file,String storeKey,int maxCacheSize)throws IOException{
        this(file,storeKey);
        super.setMaxCacheSize(maxCacheSize);
    }

	@Override
	public void put(String key, byte[] value)  {
		map.put(key, value);
        cacheOrFlush();
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
        cacheOrFlush();
	}

    @Override
    public boolean exists(String key) {
        return map.containsKey(key);
    }

    @Override
	public synchronized void clear() {
		map.clear();
        flush();
	}

}
