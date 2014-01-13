package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.tester.FireIncrementTester;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:14
 * To change this template use File | Settings | File Templates.
 */
public class FireIncrementTest {
    public static void main(String[] args) {
        FireIncrementTester test = new FireIncrementTester(Config.getFireStore(args));
        test.test();
    }
}
