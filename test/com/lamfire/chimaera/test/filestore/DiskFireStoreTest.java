package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.test.tester.FireStoreTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireStoreTest  extends DiskStore{

    public static void main(String[] args)throws Exception {
        FireStore store  =getFireStore();
        FireStoreTester test = new FireStoreTester(store);
        test.test();
    }
}
