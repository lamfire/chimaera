package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.store.filestore.DiskFireQueue;
import com.lamfire.chimaera.store.filestore.StoreEngine;
import com.lamfire.chimaera.test.tester.FireQueueTester;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireQueueTest  extends DiskStore{
    private static final String FILE = "/data/chimaera/store";

    public static void main(String[] args)throws Exception {
        StoreEngine store = getStoreEngine();;
        FireQueue queue  = new DiskFireQueue(store,"TEST_SET");
        FireQueueTester test = new FireQueueTester(queue);
        test.test();
    }
}
