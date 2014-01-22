package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.Item;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.tester.FireRankTester;
import com.lamfire.utils.Asserts;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class FireRankTest {

    public static void main(String[] args) {
        FireRankTester test = new FireRankTester(Config.getFireStore(args).getFireRank("TEST_RANK"));
        test.max(10);
        test.min(10);
        test.max(10);
        test.min(10);
        test.size();
        //test.putsRandom(99999999);
        System.exit(0);
    }
}
