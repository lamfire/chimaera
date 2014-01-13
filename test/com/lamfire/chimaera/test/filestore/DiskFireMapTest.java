package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.store.filestore.DiskFireMap;
import com.lamfire.chimaera.store.filestore.StoreEngine;
import com.lamfire.chimaera.test.tester.FireMapTester;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.RandomUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-12-18
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireMapTest {
    private static final String FILE = "/data/chimaera/store";

    public static void main(String[] args)throws Exception {
        StoreEngine store = new StoreEngine(FILE);
        FireMap map  = new DiskFireMap(store,"TEST_MAP");
        FireMapTester test = new FireMapTester(map);
        test.test();
    }
}
