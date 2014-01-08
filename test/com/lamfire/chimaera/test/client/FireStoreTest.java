package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireStore;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class FireStoreTest {
    FireStore store;

    public FireStoreTest(FireStore store){
        this.store = store;
    }

    public void test() {
        store.clear();
        System.out.println("store.clear()");

        int size = store.size();
        System.out.println("store.size() = " + size);
        Asserts.assertEquals(size,0);

    }

    public static void main(String[] args) {
        FireStoreTest test = new FireStoreTest(Config.getFireStore());
        test.test();
        Config.shutdown();
    }
}
