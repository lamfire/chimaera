package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.leveldbstore.LDBFireList;
import com.lamfire.chimaera.store.leveldbstore.LDBFireRank;
import com.lamfire.chimaera.store.leveldbstore.LDBFireSet;
import com.lamfire.chimaera.store.leveldbstore.LevelDB;
import com.lamfire.chimaera.test.benchmark.FireRankBenchmark;
import com.lamfire.chimaera.test.benchmark.FireSetBenchmark;
import com.lamfire.chimaera.test.tester.FireListTester;
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
        LevelDB levelDB = new LevelDB("/data/LevelDB_TEST1");
        levelDB.open();

        FireSet rank = new LDBFireSet(levelDB,"set_benchmark");
        FireSetBenchmark benchmark = new FireSetBenchmark(rank);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test() {
        LevelDB levelDB = new LevelDB("/data/LevelDB_TEST1");
        levelDB.open();

        FireSet set = new LDBFireSet(levelDB,"set_tester");
        FireSetTester tester = new FireSetTester(set);
        tester.test();
        levelDB.close();
    }

    public static void main(String[] args){
        if(ArrayUtils.contains(args, "benchmark")){
            benchmark();
        }else{
            test();
        }
    }
}
