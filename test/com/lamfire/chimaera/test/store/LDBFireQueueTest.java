package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.store.leveldbstore.LDBDatabase;
import com.lamfire.chimaera.store.leveldbstore.LDBFireQueue;
import com.lamfire.chimaera.store.leveldbstore.LDBManager;
import com.lamfire.chimaera.store.leveldbstore.LDBMeta;
import com.lamfire.chimaera.test.benchmark.FireQueueBenchmark;
import com.lamfire.chimaera.test.tester.FireQueueTester;
import com.lamfire.utils.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 上午10:25
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireQueueTest {
    public static void benchmark() {
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "queue_tester";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = manager.getDB(name);

        FireQueue queue = new LDBFireQueue(meta,db,name);
        FireQueueBenchmark benchmark = new FireQueueBenchmark(queue);
        benchmark.startupBenchmarkRead(1);
    }

    public static void test() {
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "queue_tester";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = manager.getDB(name);

        FireQueue queue = new LDBFireQueue(meta,db,name);
        FireQueueTester tester = new FireQueueTester(queue);
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
