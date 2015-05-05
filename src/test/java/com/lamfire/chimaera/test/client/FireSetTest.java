package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireSetBenchmark;
import com.lamfire.chimaera.test.tester.FireSetTester;
import com.lamfire.pandora.FireSet;
import com.lamfire.utils.ArrayUtils;
import com.lamfire.utils.Asserts;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class FireSetTest {

    public static void benchmark(FireSet set){
        FireSetBenchmark benchmark = new FireSetBenchmark(set);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test(FireSet set) {
        FireSetTester test = new FireSetTester(set);
        test.test();
    }

    public static void main(String[] args) {
        FireSet set =  Config.getPandora(args).getFireSet("TEST_SET");
        if(ArrayUtils.contains(args, "benchmark")){
             benchmark(set);
        }else{
            test(set);
        }
    }
}
