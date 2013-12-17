package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireList;

import java.util.*;

public class FireListInMemory implements FireList {
	
	private final List<byte[]> list = new LinkedList<byte[]>();

	@Override
	public void add(byte[] value) {
		list.add(value);
	}

    public  Iterator<byte[]> iterator(){
        return list.iterator();
    }

	@Override
	public void set(int index, byte[] value) {
		list.set(index, value);
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
		return list.remove(index);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public void clear() {
		list.clear();
	}

}
