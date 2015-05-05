package com.lamfire.chimaera.test.tester;

import com.lamfire.pandora.FireSet;
import com.lamfire.utils.Asserts;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class FireSetTester {

    FireSet set;

    public FireSetTester(FireSet set){
        this.set = set;
    }

    public  void test() {
        int elements = 100000;
        System.out.println("==>> startup : " + this.getClass().getName());
        set.clear();
        System.out.println("set.clear()");
        long startAt = System.currentTimeMillis();


        System.out.println("set.add() - " + elements);
        for(int i=0;i<elements;i++){
            String val = String.valueOf(i);
            set.add(val.getBytes());
        }
        Asserts.assertEquals(elements,set.size());
        System.out.println("set.size() = " + set.size());

        System.out.println("set.add() - " + elements);
        for(int i=0;i<elements;i++){
            String val = String.valueOf(i);
            set.add(val.getBytes());
        }
        Asserts.assertEquals(elements,set.size());
        System.out.println("set.size() = " + set.size());

        System.out.print("set.gets(2,5):" );
        List<byte[]> gets = set.gets(2,5);
        for(byte[] bytes : gets){
            String v = new String(bytes);
            System.out.print(v +" ");
        }
        System.out.println();

        System.out.print("set.gets("+(elements -10)+",10):" );
        gets = set.gets(elements -10,10);
        for(byte[] bytes : gets){
            String v = new String(bytes);
            System.out.print(v +" ");
        }
        System.out.println();


        long size = set.size();
        System.out.println("set.size():"+size);
        Asserts.assertEquals(size, elements);

        boolean exists = set.exists("44".getBytes());
        System.out.println("set.exists(44):"+exists);
        Asserts.assertTrue(exists);

        String value = new String(set.get(44));
        System.out.println("set.get(44):"+value);

        set.remove(value.getBytes()) ;
        System.out.println("set.remove(bytes):");

        exists = set.exists(value.getBytes());
        System.out.println("set.exists(44):"+exists);
        Asserts.assertFalse(exists);

        size = set.size();
        System.out.println("set.size():"+size);
        Asserts.assertEquals(elements -1,size);

        System.out.println("<<== finish : " + this.getClass().getName() +" - " +(System.currentTimeMillis() - startAt) +"ms");
    }

}
