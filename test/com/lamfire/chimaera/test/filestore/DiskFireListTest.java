package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.store.filestore.DiskFireList;
import com.lamfire.chimaera.store.filestore.StoreEngine;
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
public class DiskFireListTest {
    private static final String FILE = "/data/chimaera/store";

    public static void main(String[] args) throws Exception{
        StoreEngine store = new StoreEngine(FILE);
        FireList list  = new DiskFireList(store,"TEST_LIST");
        FireListTester test = new FireListTester(list);
        test.test();
    }
}
