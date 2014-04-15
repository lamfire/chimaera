package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.client.ChimaeraCli;
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
        //FireRankTester test = new FireRankTester(Config.getFireStore(args).getFireRank("TEST_RANK"));
//        test.max(10);
//        test.min(10);
//        test.max(10);
//        test.min(10);
//        test.size();
        //test.putsRandom(99999999);
        //test.test();
        //System.exit(0);

        ChimaeraCli cli = new ChimaeraCli();
        cli.open("127.0.0.1", 19800);
        FireRank rank = cli.getFireStore("AK_IP").getFireRank("520Test");
        System.out.println(rank.size());
        rank.incr("11233",1);
        System.out.println(cli.getFireStore("AK_IP").getFireRank("520Test").size());

        rank.remove("11233");

        List<Item> list = rank.max(10);
        for(Item item : list){
            System.out.println(item);
        }
    }
}
