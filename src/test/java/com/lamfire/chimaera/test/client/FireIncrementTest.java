package com.lamfire.chimaera.test.client;


import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireIncrementBenchmark;
import com.lamfire.chimaera.test.tester.FireIncrementTester;
import com.lamfire.pandora.FireIncrement;
import com.lamfire.utils.ArrayUtils;

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
        FireIncrement target =  Config.getPandora(args).getFireIncrement("FireIncrement");
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark(target);
        }else{
            test(target);
        }
    }

}
