package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireRankBenchmark;
import com.lamfire.chimaera.test.tester.FireRankTester;
import com.lamfire.pandora.FireRank;
import com.lamfire.utils.ArrayUtils;

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
        FireRank target =  Config.getPandora(args).getFireRank("FireRank");
        if(ArrayUtils.contains(args,"benchmark")){
            benchmark(target);
        }else{
            test(target);
        }
    }
}
