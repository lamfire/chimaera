package com.lamfire.chimaera.test.client;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 下午1:34
 * To change this template use File | Settings | File Templates.
 */
public class TestAll {
    public static void main(String[] args) {
        Config.setupByArgs(TestAll.class,args);
        FireStoreTest.test();
        FireMapTest.main(null);
        FireRankTest.main(null);
        FireIncrementTest.main(null);
        FireListTest.main(null);
        FireSetTest.main(null);
        Config.shutdown();
    }
}
