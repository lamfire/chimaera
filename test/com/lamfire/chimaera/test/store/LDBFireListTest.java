package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.store.leveldbstore.LDBDatabase;
import com.lamfire.chimaera.store.leveldbstore.LDBFireList;
import com.lamfire.chimaera.store.leveldbstore.LDBManager;
import com.lamfire.chimaera.store.leveldbstore.LDBMeta;
import com.lamfire.chimaera.test.benchmark.FireListBenchmark;
import com.lamfire.chimaera.test.tester.FireListTester;
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
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "list_benchmark";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = manager.database(name);

        FireList list = new LDBFireList(meta,db,"list_benchmark");
        FireListBenchmark benchmark = new FireListBenchmark(list);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test() {
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "list_tester";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = manager.database(name);

        FireList list = new LDBFireList(meta,db,"list_tester");
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
