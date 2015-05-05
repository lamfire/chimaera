package com.lamfire.chimaera.test.client;


import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireQueueBenchmark;
import com.lamfire.chimaera.test.tester.FireQueueTester;
import com.lamfire.pandora.FireQueue;
import com.lamfire.utils.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class FireQueueTest {

    public static void benchmark(FireQueue target){
        FireQueueBenchmark benchmark = new FireQueueBenchmark(target);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test(FireQueue target) {
        FireQueueTester test = new FireQueueTester(target);
        test.test();
    }

    public static void main(String[] args) {
        FireQueue target =  Config.getPandora(args).getFireQueue("FireQueue");
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark(target);
        }else{
            test(target);
        }
    }

}
