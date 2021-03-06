package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireListBenchmark;
import com.lamfire.chimaera.test.benchmark.FireMapBenchmark;
import com.lamfire.chimaera.test.tester.FireListTester;
import com.lamfire.chimaera.test.tester.FireMapTester;
import com.lamfire.pandora.FireMap;
import com.lamfire.utils.ArrayUtils;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.RandomUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
public class FireMapTest {

    public static void benchmark(FireMap target){
        FireMapBenchmark benchmark = new FireMapBenchmark(target);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test(FireMap target) {
        FireMapTester test = new FireMapTester(target);
        test.test();
    }

    public static void main(String[] args) {
        FireMap target =  Config.getPandora(args).getFireMap("FireMap");
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark(target);
        }else{
            test(target);
        }
    }

}
