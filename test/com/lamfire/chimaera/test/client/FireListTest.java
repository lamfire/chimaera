package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.test.Config;
import com.lamfire.chimaera.test.benchmark.FireIncrementBenchmark;
import com.lamfire.chimaera.test.benchmark.FireListBenchmark;
import com.lamfire.chimaera.test.tester.FireIncrementTester;
import com.lamfire.chimaera.test.tester.FireListTester;
import com.lamfire.utils.Asserts;

import java.util.List;

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
        FireList target =  Config.getFireStore(args).getFireList("FireList");
        if(args.length== 0){
            benchmark(target);
        }else{
            test(target);
        }
    }
}
