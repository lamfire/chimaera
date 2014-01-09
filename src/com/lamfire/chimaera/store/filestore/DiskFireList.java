package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DiskFireList implements FireList {
	private final List<byte[]> list;
    private StoreEngine engine;
    private String name;
    public DiskFireList(StoreEngine engine, String name){
        this.engine = engine;
        this.name = name;
        this.list = engine.getLinkedList(name,new BytesSerializer());
    }

	@Override
	public void add(byte[] value) {
		list.add(value);
        engine.cacheOrFlush();
	}

    public  Iterator<byte[]> iterator(){
        return list.iterator();
    }

	@Override
	public void set(int index, byte[] value) {
		list.set(index, value);
        engine.cacheOrFlush();
	}

	@Override
	public byte[] get(int index) {
		return list.get(index);
	}

    @Override
    public List<byte[]> gets(int fromIndex,int size) {
        List<byte[]> result = new ArrayList<byte[]>();
        for(int i=fromIndex;i< list.size();i++) {
            result.add(list.get(i));
            if(result.size() >= size){
               break;
            }
        }
       return result;
    }

	@Override
	public byte[] remove(int index) {
		byte[] bytes =  list.remove(index);
        engine.flush();
        return bytes;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public void clear() {
		list.clear();
        //engine.flush();
	}

}
