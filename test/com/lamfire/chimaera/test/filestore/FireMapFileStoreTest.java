package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.store.filestore.FireMapInFile;
import com.lamfire.chimaera.test.client.Config;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.RandomUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-12-18
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public class FireMapFileStoreTest {
    private static FireMap map ;

    public static synchronized FireMap get()throws Exception{
        if(map != null)return map;
        map = new FireMapInFile("/data/chimaera/map","map");
        return map;
    }

    public static void test() throws Exception{
        FireMap map = get();
        String field = "TEST1";
        String value = "22222222222222";
        long startAt = System.currentTimeMillis();
        map.put(field, value.getBytes());
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("map.put('"+field+"','"+value+"') time_millis:" + timeUsed+ " ms");

        field = "TEST0";
        value =  RandomUtils.randomTextWithFixedLength(1024);
        startAt = System.currentTimeMillis();
        map.put(field, value.getBytes());
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("map.put('"+field+"','"+value+"') time_millis:" + timeUsed+ " ms");


        int size = map.size();
        System.out.println("map.size():" + size);
        Asserts.assertEquals(2, size);


        startAt = System.currentTimeMillis();
        value = new String(map.get(field));
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("map.get("+field+"):"+value+" ,time_millis:" + timeUsed + " ms");

        startAt = System.currentTimeMillis();
        field = "TEST1";
        value =new String(map.get(field));
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("map.get("+field+"):"+value+" ,time_millis:" + timeUsed + " ms");
        Asserts.assertEquals(value, "22222222222222");

        startAt = System.currentTimeMillis();
        boolean exists =  map.exists(field);
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("map.exists("+field+") :"+exists+",time_millis:" + timeUsed + " ms");
        Asserts.assertTrue(exists);


        startAt = System.currentTimeMillis();
        map.remove(field);
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("map.remove("+field +") time_millis:" + timeUsed + " ms");

        size = map.size();
        System.out.println("map.size():" + size);
        Asserts.assertEquals(1, size);


        startAt = System.currentTimeMillis();
        exists =  map.exists(field);
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("map.exists("+field+") :"+exists+",time_millis:" + timeUsed + " ms");
        Asserts.assertFalse(exists);

        List<String> keys = map.keys();
        System.out.println(keys);
    }

    public static void main(String[] args)throws Exception {
        //test();
        FireMap map = get();
        System.out.println(map.keys());
    }
}
