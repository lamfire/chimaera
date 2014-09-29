package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireMapBenchmark;
import com.lamfire.chimaera.test.benchmark.FireQueueBenchmark;
import com.lamfire.chimaera.test.tester.FireMapTester;
import com.lamfire.chimaera.test.tester.FireQueueTester;
import com.lamfire.utils.Asserts;

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
        FireQueue target =  Config.getFireStore(args).getFireQueue("FireQueue");
        if(args.length== 0){
            benchmark(target);
        }else{
            test(target);
        }
    }

}
