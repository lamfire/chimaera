package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.utils.Lists;

import java.util.*;

public class MemoryFireMap implements FireMap {
	
	private final Map<String,byte[]> map = new LinkedHashMap<String, byte[]>();

	@Override
	public void put(String key, byte[] value) {
		map.put(key, value);
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
	}

    @Override
    public boolean exists(String key) {
        return map.containsKey(key);
    }

    @Override
	public void clear() {
		map.clear();
	}

}
