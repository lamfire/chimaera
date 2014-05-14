package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.test.tester.FireMapTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-12-18
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireMapTest  extends DiskStore{
    public static void main(String[] args)throws Exception {
        FireMap map  = getFireStore().getFireMap("TEST_MAP");
        FireMapTester test = new FireMapTester(map);
        test.test();
    }
}
