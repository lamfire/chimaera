package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.tester.FireListTester;
import com.lamfire.utils.Asserts;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class FireListTest {

    public static void main(String[] args) {
        FireList list = Config.getFireStore(args).getFireList("TEST_LIST");
        FireListTester test = new FireListTester(list);
        test.test();
        Config.getChimaeraCli().close();
    }
}
