package com.lamfire.chimaera.test.dbdengine.bdb;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.test.tester.FireSetTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class BDBFireSetTest extends BDBStore {
    private static final String FILE = "/data/chimaera/groupuser";

    public static void main(String[] args)throws Exception {
        FireSet set  =getFireStore().getFireSet("5354d4d87fbeffb3213fed64");
        new FireSetTester(set).test();
    }
}
