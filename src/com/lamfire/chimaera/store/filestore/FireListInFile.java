package com.lamfire.chimaera.store.filestore;

import com.lamfire.chimaera.store.FireList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FireListInFile implements FireList {
	private final List<byte[]> list;
    private FileStore store;
    private String name;
    public FireListInFile(FileStore store,String name){
        this.store = store;
        this.name = name;
        this.list = store.getLinkedList(name);
    }

	@Override
	public void add(byte[] value) {
		list.add(value);
        store.cacheOrFlush();
	}

    public  Iterator<byte[]> iterator(){
        return list.iterator();
    }

	@Override
	public void set(int index, byte[] value) {
		list.set(index, value);
        store.cacheOrFlush();
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
        store.flush();
        return bytes;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public void clear() {
		list.clear();
        store.flush();
	}

}
