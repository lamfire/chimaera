package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.Item;
import com.lamfire.chimaera.store.filestore.DiskFireRank;
import com.lamfire.chimaera.store.filestore.StoreEngine;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.tester.FireRankTester;
import com.lamfire.utils.Asserts;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireRankTest {
    private static final String FILE = "/data/chimaera/store";

    public static void main(String[] args) throws IOException {
        StoreEngine store = new StoreEngine(FILE,true);
        FireRankTester test = new FireRankTester(new DiskFireRank(store,"TEST_RANK"));
        //test.test();
        test.incr();
        test.max(5);
        test.putsRandom(20);
        test.max(5);
        test.size();

    }
}
