package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.tester.FireQueueTester;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class FireQueueTest {

    public static void main(String[] args) {
        FireQueueTester test = new FireQueueTester(Config.getFireStore(args).getFireQueue("TEST_QUEUE"));
        test.test();
    }
}
