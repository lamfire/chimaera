package com.lamfire.chimaera.test.dbdengine.leveldb;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.store.leveldbstore.LDBFireIncrement;
import com.lamfire.chimaera.store.leveldbstore.LDBFireList;
import com.lamfire.chimaera.store.leveldbstore.LevelDB;
import com.lamfire.chimaera.test.benchmark.FireIncrementBenchmark;
import com.lamfire.chimaera.test.benchmark.FireListBenchmark;
import com.lamfire.chimaera.test.tester.FireIncrementTester;
import com.lamfire.chimaera.test.tester.FireListTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午3:53
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireIncrementTest {

    public static void benchmark() {
        LevelDB levelDB = new LevelDB("K:/LevelDB_TEST1");
        levelDB.open();

        FireIncrement increment = new LDBFireIncrement(levelDB,"increment_benchmark");
        FireIncrementBenchmark benchmark = new FireIncrementBenchmark(increment);
        benchmark.startupBenchmarkRead(1);
    }

    public static void test() {
        LevelDB levelDB = new LevelDB("K:/LevelDB_TEST1");
        levelDB.open();

        FireIncrement increment = new LDBFireIncrement(levelDB,"increment_tester");
        FireIncrementTester tester = new FireIncrementTester(increment);
        tester.test();
        levelDB.close();
    }

    public static void main(String[] args){
        benchmark();
    }
}
