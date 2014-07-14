package com.lamfire.chimaera.store.bdbstore;

import com.lamfire.chimaera.store.FireSet;
import com.sleepycat.collections.StoredKeySet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-7-14
 * Time: 下午1:30
 * To change this template use File | Settings | File Templates.
 */
public class BDBFireSet implements FireSet {

    private BDBEngine engine;
    private StoredKeySet<byte[]> set;
    private String name;

    public BDBFireSet(BDBEngine engine ,String name){
        this.engine = engine;
        this.name = name;
        set = engine.getSet(name);
    }


    @Override
    public void add(byte[] value) {
        set.add(value);
    }

    @Override
    public byte[] remove(int index) {
        byte[] bytes = get(index);
        if (bytes != null) {
            remove(bytes);
        }
        return bytes;
    }

    @Override
    public byte[] remove(byte[] value) {
        set.remove(value);
        return value;
    }

    @Override
    public byte[] get(int index) {
        int i=0;
        for(byte[] e:set){
            if(i == index){
                return e;
            }
            i++;
        }
        return null;
    }

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        List<byte[]> list = new ArrayList<byte[]>();
        int i=0;
        for(byte[] e:set){
            if(i >= fromIndex){
                list.add(e);
                if(list.size() == size){
                    break;
                }
            }
            i++;
        }

        return list;
    }

    @Override
    public boolean exists(byte[] bytes) {
        return set.contains(bytes);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public void clear() {
        set.clear();
    }
}
