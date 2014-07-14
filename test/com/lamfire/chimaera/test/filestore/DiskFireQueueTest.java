package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.test.tester.FireQueueTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireQueueTest  extends DiskStore{
    public static void main(String[] args)throws Exception {
        FireQueue queue  = getFireStore().getFireQueue("TEST_SET");
        FireQueueTester test = new FireQueueTester(queue);
        for(int i=0;i<100;i++)
        test.test();
    }
}
