package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.tester.FireMapTester;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.RandomUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
public class FireMapTest {

    public static void main(String[] args) {
        FireMapTester test = new FireMapTester(Config.getFireStore(args).getFireMap("TEST_MAP"));
        test.test();
    }
}
