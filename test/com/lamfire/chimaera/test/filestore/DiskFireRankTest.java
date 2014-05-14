package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.test.tester.FireRankTester;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireRankTest  extends DiskStore{

    public static void main(String[] args) throws IOException {
        FireRankTester test = new FireRankTester(getFireStore().getFireRank("TEST_RANK"));
        test.size();
        test.test();
        test.incr();
        test.max(5);
        test.putsRandom(20);
        test.max(5);
        test.size();

    }
}
