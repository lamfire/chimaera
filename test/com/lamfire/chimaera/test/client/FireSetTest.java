package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.test.Config;
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
public class FireSetTest {

    public static void main(String[] args) {
        FireSet set = Config.getFireStore(args).getFireSet("TEST_SET");
        FireSetTester test = new FireSetTester(set);
        test.test();
    }
}
