package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.filestore.DiskFireSet;
import com.lamfire.chimaera.store.filestore.StoreEngine;
import com.lamfire.utils.Asserts;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class FireSetFileStoreTest {
    private static final String FILE = "/data/chimaera/store";
    private FireSet set;

    public FireSetFileStoreTest(FireSet set){
        this.set = set;
    }

    public void test()throws Exception {
        set.clear();
        System.out.println("set.clear()");

        for(int i=0;i<100;i++){
            String val = String.valueOf(i);
            set.add(val.getBytes());
            System.out.println("set.add("+val+")");
        }

        for(int i=0;i<100;i++){
            String val = String.valueOf(i);
            set.add(val.getBytes());
            System.out.println("set.add(" + val + ")");
        }

        System.out.print("set.gets(2,5):" );
        List<byte[]> gets = set.gets(2,5);
        for(byte[] bytes : gets){
            String v = new String(bytes);
            System.out.print(v +" ");
        }
        System.out.println();


        int size = set.size();
        System.out.println("set.size():"+size);
        Asserts.assertEquals(size, 100);

        boolean exists = set.exists("44".getBytes());
        System.out.println("set.exists(44):"+exists);
        Asserts.assertTrue(exists);

        String value = new String(set.get(44));
        System.out.println("set.get(44):"+value);
        Asserts.assertEquals(value, "44");

        value = new String(set.remove("44".getBytes()));
        System.out.println("set.remove(44):"+value);
        Asserts.assertEquals(value, "44");

        value = new String(set.get(44));
        System.out.println("set.get(44):"+value);
        Asserts.assertEquals(value, "45");

        exists = set.exists("44".getBytes());
        System.out.println("set.exists(44):"+exists);
        Asserts.assertFalse(exists);

        size = set.size();
        System.out.println("set.size():"+size);
        Asserts.assertEquals(99,size);

        value =  new String(set.get(98));
        System.out.println("set.get(98):"+value);
        Asserts.assertEquals("99",value);

    }

    public static void main(String[] args)throws Exception {
        StoreEngine store = new StoreEngine(FILE);
        FireSet set  = new DiskFireSet(store,"TEST_SET");
        FireSetFileStoreTest test = new FireSetFileStoreTest(set);
        test.test();
    }
}
