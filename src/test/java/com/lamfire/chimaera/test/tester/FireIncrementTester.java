package com.lamfire.chimaera.test.tester;


import com.lamfire.pandora.FireIncrement;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:14
 * To change this template use File | Settings | File Templates.
 */
public class FireIncrementTester {
    FireIncrement inc;

    public FireIncrementTester( FireIncrement inc){
       this.inc =inc ;
    }

    public void test() {
        System.out.println("==>> startup : " + this.getClass().getName());
        String name = "INC_001";

        inc.clear();
        Asserts.equalsAssert(0, inc.size());

        inc.set(name,0);
        Asserts.equalsAssert(1, inc.size());

        inc.remove(name);
        Asserts.equalsAssert(0, inc.size());


        long startAt = System.currentTimeMillis();
        int count = 1000;
        int seed = 10;
        for(int i=0;i<count;i++){
            inc.incrGet(name,seed);
        }
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("increment.incrGet() times:"+count+",time_millis:" + timeUsed+ " ms");

        long value = inc.get(name);
        System.out.println("increment.get() : " +value);

        Asserts.equalsAssert(value, count * seed);

        startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            inc.incr(name, 0 - seed);
        }

        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("increment.decrGet() times:"+count+",time_millis:" + timeUsed+ " ms");

        value = inc.get(name);
        System.out.println("increment.get() : " +value);

        Asserts.equalsAssert(value,0);

        startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            inc.incr(name,seed);
        }
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("increment.incr() times:"+count+" time_millis:" + timeUsed+ " ms");

        value = inc.get(name);
        System.out.println("increment.get() : " +value);

        startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            inc.incr(name, 0 - seed);
        }

        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("increment.decr() times:"+count+" time_millis:" + timeUsed+ " ms");

        value =inc.get(name);
        System.out.println("final increment.get():"+value);
        Asserts.equalsAssert(value,0);

        System.out.println("<<== finish : " + this.getClass().getName());
    }
}
