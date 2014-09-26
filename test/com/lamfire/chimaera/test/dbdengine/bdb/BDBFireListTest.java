package com.lamfire.chimaera.test.dbdengine.bdb;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.test.tester.FireListTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class BDBFireListTest extends BDBStore {

    public static void main(String[] args) throws Exception{
        FireList list  = getFireStore().getFireList("TEST_LIST");
        FireListTester test = new FireListTester(list);
        test.test();
    }
}
