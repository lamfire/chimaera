package com.lamfire.chimaera.test.dbdengine.bdb;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.test.tester.FireIncrementTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class BDBFireIncrementTest extends BDBStore {
    public static void main(String[] args)throws Exception {
        FireIncrement inc  = getFireStore().getFireIncrement("TEST_INCR");
        FireIncrementTester test = new FireIncrementTester(inc);
        test.test();
    }
}
