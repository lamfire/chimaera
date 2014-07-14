package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.test.tester.FireListTester;
import com.lamfire.utils.Threads;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireListTest extends DiskStore{

    public static void main(String[] args) throws Exception{
        FireList list  = getFireStore().getFireList("TEST_LIST");
        FireListTester test = new FireListTester(list);
        for(int i=0;i<1000;i++){
            test.test();
        }
    }
}
