package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.store.leveldbstore.LDBFireMap;
import com.lamfire.chimaera.store.leveldbstore.LevelDB;
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
        LevelDB levelDB = new LevelDB("/data/LevelDB_TEST1");
        levelDB.open();

        FireMap map = new LDBFireMap(levelDB,"map_benchmark");
        FireMapBenchmark benchmark = new FireMapBenchmark(map);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test() throws Exception {
        LevelDB levelDB = new LevelDB("/data/LevelDB_TEST1");
        levelDB.open();
        FireMap map = new LDBFireMap(levelDB,"map_tester");

        FireMapTester tester = new FireMapTester(map);
        tester.test();
        levelDB.close();
    }

    public static void main(String[] args) throws Exception {
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark();
        }else{
            test();
        }
    }
}
