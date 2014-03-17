package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.filestore.DiskFireIncrement;
import com.lamfire.chimaera.store.filestore.DiskFireSet;
import com.lamfire.chimaera.store.filestore.StoreEngine;
import com.lamfire.chimaera.test.tester.FireIncrementTester;
import com.lamfire.chimaera.test.tester.FireSetTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireIncrementTest extends DiskStore{
    private static final String FILE = "/data/chimaera/store";

    public static void main(String[] args)throws Exception {
        StoreEngine store = getStoreEngine();
        FireIncrement inc  = new DiskFireIncrement(store,"TEST_INCR");
        FireIncrementTester test = new FireIncrementTester(inc);
        test.test();
    }
}
