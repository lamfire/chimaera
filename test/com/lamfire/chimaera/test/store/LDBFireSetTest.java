package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.leveldbstore.LDBDatabase;
import com.lamfire.chimaera.store.leveldbstore.LDBFireSet;
import com.lamfire.chimaera.store.leveldbstore.LDBManager;
import com.lamfire.chimaera.store.leveldbstore.LDBMeta;
import com.lamfire.chimaera.test.benchmark.FireSetBenchmark;
import com.lamfire.chimaera.test.tester.FireSetTester;
import com.lamfire.utils.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午6:24
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireSetTest {

    public static void benchmark(){
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "set_benchmark";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = new LDBDatabase(manager,name);
        FireSet rank = new LDBFireSet(meta,db,name);
        FireSetBenchmark benchmark = new FireSetBenchmark(rank);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test() {
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "set_tester";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = new LDBDatabase(manager,name);
        FireSet set = new LDBFireSet(meta,db,name);
        FireSetTester tester = new FireSetTester(set);
        tester.test();
        manager.closeAll();
    }

    public static void main(String[] args){
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark();
        }else{
            test();
        }
    }
}
