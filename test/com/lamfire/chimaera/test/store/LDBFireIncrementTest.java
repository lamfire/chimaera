package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.leveldbstore.LDBDatabase;
import com.lamfire.chimaera.store.leveldbstore.LDBFireIncrement;
import com.lamfire.chimaera.store.leveldbstore.LDBManager;
import com.lamfire.chimaera.store.leveldbstore.LDBMeta;
import com.lamfire.chimaera.test.benchmark.FireIncrementBenchmark;
import com.lamfire.chimaera.test.tester.FireIncrementTester;
import com.lamfire.utils.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午3:53
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireIncrementTest {

    public static void benchmark() {
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "increment_benchmark";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = manager.database(name);

        FireIncrement increment = new LDBFireIncrement(meta,db,"increment_benchmark");
        FireIncrementBenchmark benchmark = new FireIncrementBenchmark(increment);
        benchmark.startupBenchmarkRead(1);
    }

    public static void test() {
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "increment_tester";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = manager.database(name);

        FireIncrement increment = new LDBFireIncrement(meta,db,"increment_tester");
        FireIncrementTester tester = new FireIncrementTester(increment);
        tester.test();
    }

    public static void main(String[] args){
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark();
        }else{
            test();
        }
    }
}
