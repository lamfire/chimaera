package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.FireSet;
import com.lamfire.chimaera.store.leveldbstore.LDBFireRank;
import com.lamfire.chimaera.store.leveldbstore.LDBFireSet;
import com.lamfire.chimaera.store.leveldbstore.LevelDB;
import com.lamfire.chimaera.test.benchmark.FireRankBenchmark;
import com.lamfire.chimaera.test.tester.FireRankTester;
import com.lamfire.chimaera.test.tester.FireSetTester;
import com.lamfire.utils.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午6:24
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireRankTest {

    public static void benchmark(){
        LevelDB levelDB = new LevelDB("/data/LevelDB_TEST1");
        levelDB.open();

        FireRank rank = new LDBFireRank(levelDB,"rank_benchmark");
        FireRankBenchmark benchmark = new FireRankBenchmark(rank);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test() {
        LevelDB levelDB = new LevelDB("/data/LevelDB_TEST1");
        levelDB.open();

        FireRank set = new LDBFireRank(levelDB,"rank_tester");
        FireRankTester tester = new FireRankTester(set);
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
