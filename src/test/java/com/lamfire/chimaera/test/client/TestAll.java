package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.tester.*;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 下午1:34
 * To change this template use File | Settings | File Templates.
 */
public class TestAll {
    public static void test(String[] args){
        FireIncrementTester t1 = new FireIncrementTester(Config.getFireStore(args).getFireIncrement("TEST_INCR"));
        t1.test();

        FireListTester t2 = new FireListTester(Config.getFireStore(args).getFireList("TEST_LIST"));
        t2.test();

        FireMapTester t3 = new FireMapTester(Config.getFireStore(args).getFireMap("TEST_MAP"));
        t3.test();

        FireQueueTester t4 = new FireQueueTester(Config.getFireStore(args).getFireQueue("TEST_QUEUE"));
        t4.test();

        FireRankTester t5 = new FireRankTester(Config.getFireStore(args).getFireRank("TEST_RANK"));
        t5.test();

        FireSetTester t6 = new FireSetTester( Config.getFireStore(args).getFireSet("TEST_SET"));
        t6.test();

        FireStoreTester t7 = new FireStoreTester(Config.getFireStore(args));
        t7.test();
    }
    public static void main(String[] args) {
        for(int i=0;i<1;i++){
            test(args);
        }
        Config.shutdown();
    }
}
