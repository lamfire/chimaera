package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.store.leveldbstore.LDBFireList;
import com.lamfire.chimaera.store.leveldbstore.LDBFireQueue;
import com.lamfire.chimaera.store.leveldbstore.LevelDB;
import com.lamfire.chimaera.test.benchmark.FireListBenchmark;
import com.lamfire.chimaera.test.benchmark.FireQueueBenchmark;
import com.lamfire.chimaera.test.tester.FireListTester;
import com.lamfire.chimaera.test.tester.FireQueueTester;
import com.lamfire.utils.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午3:13
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireListTest {
    public static void benchmark() {
        LevelDB levelDB = new LevelDB("/data/LevelDB_TEST1");
        levelDB.open();

        FireList list = new LDBFireList(levelDB,"list_benchmark");
        FireListBenchmark benchmark = new FireListBenchmark(list);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test() {
        LevelDB levelDB = new LevelDB("/data/LevelDB_TEST1");
        levelDB.open();

        FireList list = new LDBFireList(levelDB,"list_tester");
        FireListTester tester = new FireListTester(list);
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
