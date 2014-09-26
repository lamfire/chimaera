package com.lamfire.chimaera.test.dbdengine.leveldb;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.store.leveldbstore.LDBFireQueue;
import com.lamfire.chimaera.store.leveldbstore.LevelDB;
import com.lamfire.chimaera.test.benchmark.FireQueueBenchmark;
import com.lamfire.chimaera.test.tester.FireQueueTester;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 上午10:25
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireQueueTest {
    public static void benchmark() {
        LevelDB levelDB = new LevelDB("K:/LevelDB_TEST1");
        levelDB.open();

        FireQueue queue = new LDBFireQueue(levelDB,"queue_tester");
        FireQueueBenchmark benchmark = new FireQueueBenchmark(queue);
        benchmark.startupBenchmarkRead(1);
    }

    public static void test() {
        LevelDB levelDB = new LevelDB("K:/LevelDB_TEST1");
        levelDB.open();

        FireQueue queue = new LDBFireQueue(levelDB,"queue_tester");
        FireQueueTester tester = new FireQueueTester(queue);
        tester.test();
    }

    public static void main(String[] args){
        benchmark();
    }
}
