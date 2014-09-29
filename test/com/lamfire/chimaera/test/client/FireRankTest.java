package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.Item;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireQueueBenchmark;
import com.lamfire.chimaera.test.benchmark.FireRankBenchmark;
import com.lamfire.chimaera.test.tester.FireQueueTester;
import com.lamfire.chimaera.test.tester.FireRankTester;
import com.lamfire.utils.ArrayUtils;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.StringUtils;

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

    public static void benchmark(FireRank target){
        FireRankBenchmark benchmark = new FireRankBenchmark(target);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test(FireRank target) {
        FireRankTester test = new FireRankTester(target);
        test.test();
    }

    public static void main(String[] args) {
        FireRank target =  Config.getFireStore(args).getFireRank("FireRank");
        if(ArrayUtils.contains(args,"benchmark")){
            benchmark(target);
        }else{
            test(target);
        }
    }
}
