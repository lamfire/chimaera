package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.filestore.DiskFireSet;
import com.lamfire.chimaera.store.filestore.StoreEngine;
import com.lamfire.chimaera.test.tester.FireSetTester;
import com.lamfire.utils.Asserts;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireSetTest {
    private static final String FILE = "/data/chimaera/store";

    public static void main(String[] args)throws Exception {
        StoreEngine store = new StoreEngine(FILE);
        FireSet set  = new DiskFireSet(store,"TEST_SET");
        FireSetTester test = new FireSetTester(set);
        test.test();
    }
}
