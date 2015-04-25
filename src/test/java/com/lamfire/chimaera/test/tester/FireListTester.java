package com.lamfire.chimaera.test.tester;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.Asserts;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class FireListTester {
    private FireList list;

    public FireListTester(FireList list){
        this.list = list;
    }
    public void test() {
        System.out.println("==>> startup : " + this.getClass().getName());
        list.clear();
        System.out.println("list.clear()");
        long startAt = System.currentTimeMillis();
        int elements = 1000;

        long size = list.size();
        System.out.println("list.size():"+size);
        Asserts.assertEquals(size,0);

        for(int i=0;i<elements;i++){
            String val = String.valueOf(i);
            list.add(val.getBytes());
            System.out.println("list.add("+val+")");
        }

        for(int i=0;i<elements;i++){
            String val = new String( list.get(i) ) ;
            System.out.println("list.get("+val+")");
            Asserts.assertEquals( String.valueOf(i),val);
        }

        System.out.print("list.gets(2,5):" );
        List<byte[]> gets = list.gets(2,5);
        int i=0;
        for(byte[] bytes : gets){
            String v = new String(bytes);
            System.out.print(v +" ");
            Asserts.assertEquals( String.valueOf(2+i),v);
            i++;
        }

        System.out.println();

        size = list.size();
        System.out.println("list.size():"+size);
        Asserts.assertEquals(size,elements);

        String value = new String(list.get(44));
        System.out.println("list.get(44):"+value);
        Asserts.assertEquals(value,"44");

        list.set(44,("1231231312313").getBytes());
        System.out.println("list.set(44,'1231231312313')");

        value = new String(list.get(44));
        System.out.println("list.get(44):"+value);
        Asserts.assertEquals(value,"1231231312313");

        size = list.size();
        System.out.println("list.size():"+size);
        Asserts.assertEquals(size,elements);

        //=================================================================remove
        list.remove(44);
        System.out.println("list.remove(44)");

        value = new String(list.get(44));
        System.out.println("list.get(44):"+value);
        Asserts.assertEquals(value,"45");

        size = list.size();
        System.out.println("list.size():"+size);
        Asserts.assertEquals(size,elements - 1);

        System.out.println("<<== finish : " + this.getClass().getName() + " - " + ( System.currentTimeMillis() - startAt) +"ms");
    }
}
