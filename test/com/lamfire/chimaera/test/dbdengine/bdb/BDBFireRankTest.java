package com.lamfire.chimaera.test.dbdengine.bdb;

import com.lamfire.chimaera.test.tester.FireRankTester;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class BDBFireRankTest extends BDBStore {

    public static void main(String[] args) throws IOException {
        FireRankTester test = new FireRankTester(getFireStore().getFireRank("TEST_RANK"));
        test.test();
    }
}
