package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.tester.FireStoreTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class FireStoreTest {

    public static void main(String[] args) {
        FireStoreTester test = new FireStoreTester(Config.getPandora(args));
        test.test();
    }
}
