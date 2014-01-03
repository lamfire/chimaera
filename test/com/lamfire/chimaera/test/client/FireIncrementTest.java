package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:14
 * To change this template use File | Settings | File Templates.
 */
public class FireIncrementTest {
    FireStore store;

    FireIncrementTest(FireStore store){
       this.store =store ;
    }

    public void test() {
        store.remove("INCREMENT_TEST");
        FireIncrement inc = store.getFireIncrement("INCREMENT_TEST");
        long startAt = System.currentTimeMillis();
        int count = 1000;
        int seed = 10;
        for(int i=0;i<count;i++){
            inc.incrGet(seed);
        }
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("increment.incrGet() times:"+count+",time_millis:" + timeUsed+ " ms");

        long value = inc.get();
        System.out.println("increment.get() : " +value);

        Asserts.assertEquals(value, count * seed);

        startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            inc.decrGet(seed);
        }

        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("increment.decrGet() times:"+count+",time_millis:" + timeUsed+ " ms");

        value = inc.get();
        System.out.println("increment.get() : " +value);

        Asserts.assertEquals(value,0);

        startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            inc.incr(seed);
        }
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("increment.incr() times:"+count+" time_millis:" + timeUsed+ " ms");

        value = inc.get();
        System.out.println("increment.get() : " +value);

        startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            inc.decr(seed);
        }

        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("increment.decr() times:"+count+" time_millis:" + timeUsed+ " ms");

        value =inc.get();
        System.out.println("final increment.get():"+value);
        Asserts.assertEquals(value,0);
    }

    public static void main(String[] args) {
        Config.setupByArgs(FireIncrementTest.class,args);
        FireIncrementTest test = new FireIncrementTest(Config.getFireStore());
        test.test();
        Config.shutdown();
    }
}
