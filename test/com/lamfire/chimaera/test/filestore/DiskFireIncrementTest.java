package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.filestore.DiskDatabase;
import com.lamfire.chimaera.store.filestore.DiskFireIncrement;
import com.lamfire.chimaera.test.tester.FireIncrementTester;

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
        DiskDatabase store = getDatabase(FILE);
        FireIncrement inc  = new DiskFireIncrement(store,"TEST_INCR");
        FireIncrementTester test = new FireIncrementTester(inc);
        test.test();
    }
}
