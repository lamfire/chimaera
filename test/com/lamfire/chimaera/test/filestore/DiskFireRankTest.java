package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.filestore.DiskFireRank;
import com.lamfire.chimaera.test.tester.FireRankTester;
import com.lamfire.thalia.ThaliaDatabase;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class DiskFireRankTest  extends DiskStore{
    private static final String FILE = "/data/chimaera/store";

    public static void main(String[] args) throws IOException {
        ThaliaDatabase store = getThaliaDatabase(FILE);
        FireRankTester test = new FireRankTester(new DiskFireRank(store,"TEST_RANK"));
        test.size();
        //test.test();
        test.incr();
        test.max(5);
        test.putsRandom(20);
        test.max(5);
        test.size();

    }
}
