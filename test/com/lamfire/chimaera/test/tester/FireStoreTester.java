package com.lamfire.chimaera.test.tester;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.Asserts;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class FireStoreTester {
    FireStore store;

    public FireStoreTester(FireStore store){
        this.store = store;
    }

    public long size(){
        long size = store.count();
        System.out.println("store.size() = " + size);
        return size;
    }

    public void keys(){
        Set<String> keys = store.keys();
        System.out.println("store.keys() = " + keys);
    }

    public void remove(String key){
        store.remove(key);
    }

    public void test() {
        System.out.println("==>> startup : " + this.getClass().getName());
        keys();

        size();
        //Asserts.assertEquals(size,0);

        FireRank rank = store.getFireRank("STORE_RANK_TEST");
        rank.put("1");
        keys();
        size();

        FireMap map = store.getFireMap("STORE_MAP_TEST");
        map.put("1","1".getBytes());
        keys();
        size();

        remove("STORE_RANK_TEST");
        keys();
        size();

        remove("STORE_MAP_TEST");
        keys();
        size();

        System.out.println("<<== finish : " + this.getClass().getName());
    }
}
