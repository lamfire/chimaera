package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.store.leveldbstore.LDBDatabase;
import com.lamfire.chimaera.store.leveldbstore.LDBFireMap;
import com.lamfire.chimaera.store.leveldbstore.LDBManager;
import com.lamfire.chimaera.store.leveldbstore.LDBMeta;
import com.lamfire.chimaera.test.benchmark.FireMapBenchmark;
import com.lamfire.chimaera.test.tester.FireMapTester;
import com.lamfire.utils.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-23
 * Time: 下午6:19
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireMapTest {

    public static void benchmark() throws Exception {
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "map_benchmark";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = new LDBDatabase(manager,name);

        FireMap map = new LDBFireMap(meta,db,"map_benchmark");
        FireMapBenchmark benchmark = new FireMapBenchmark(map);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test() throws Exception {
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "map_tester";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = new LDBDatabase(manager,name);

        FireMap map = new LDBFireMap(meta,db,name);

        FireMapTester tester = new FireMapTester(map);
        tester.test();
    }

    public static void main(String[] args) throws Exception {
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark();
        }else{
            test();
        }
    }
}
