package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireIncrementBenchmark;
import com.lamfire.chimaera.test.benchmark.FireSetBenchmark;
import com.lamfire.chimaera.test.tester.FireIncrementTester;
import com.lamfire.chimaera.test.tester.FireSetTester;
import com.lamfire.utils.ArrayUtils;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:14
 * To change this template use File | Settings | File Templates.
 */
public class FireIncrementTest {

    public static void benchmark(FireIncrement target){
        FireIncrementBenchmark benchmark = new FireIncrementBenchmark(target);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test(FireIncrement target) {
        FireIncrementTester test = new FireIncrementTester(target);
        test.test();
    }

    public static void main(String[] args) {
        FireIncrement target =  Config.getFireStore(args).getFireIncrement("FireIncrement");
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark(target);
        }else{
            test(target);
        }
    }

}
