package com.lamfire.chimaera.test.client;


import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireListBenchmark;
import com.lamfire.chimaera.test.tester.FireListTester;
import com.lamfire.pandora.FireList;
import com.lamfire.utils.ArrayUtils;


/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class FireListTest {

    public static void benchmark(FireList target){
        FireListBenchmark benchmark = new FireListBenchmark(target);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test(FireList target) {
        FireListTester test = new FireListTester(target);
        test.test();
    }

    public static void main(String[] args) {
        FireList target =  Config.getPandora(args).getFireList("FireList");
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark(target);
        }else{
            test(target);
        }
    }
}
