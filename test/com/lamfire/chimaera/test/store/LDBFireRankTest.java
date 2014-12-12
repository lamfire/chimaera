package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.Item;
import com.lamfire.chimaera.store.leveldbstore.LDBDatabase;
import com.lamfire.chimaera.store.leveldbstore.LDBFireRank;
import com.lamfire.chimaera.store.leveldbstore.LDBManager;
import com.lamfire.chimaera.store.leveldbstore.LDBMeta;
import com.lamfire.chimaera.test.benchmark.FireRankBenchmark;
import com.lamfire.chimaera.test.tester.FireRankTester;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-24
 * Time: 下午6:24
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireRankTest {

    public static void benchmark(){
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "rank_benchmark";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = manager.getDB(name);
        LDBDatabase idx = manager.getDB(name+"_idx");

        FireRank rank = new LDBFireRank(meta,db,idx,name);
        FireRankBenchmark benchmark = new FireRankBenchmark(rank);
        benchmark.startupBenchmarkWrite(1);
    }

    public static void test() {
        LDBManager manager = new LDBManager("/data/LevelDB_TEST1");
        String name = "rank_tester";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = manager.getDB(name);
        LDBDatabase idx = manager.getDB(name+"_idx");

        FireRank set = new LDBFireRank(meta,db,idx,name);
        FireRankTester tester = new FireRankTester(set);
        tester.test();
    }

    public static void main(String[] args){
//        if(ArrayUtils.contains(args, "benchmark")){
//            benchmark();
//        }else{
//            test();
//        }

        LDBManager manager = new LDBManager("/data/LevelDB_TEST3");
        String name = "rank_tester";
        LDBMeta meta = new LDBMeta(manager);
        LDBDatabase db = manager.getDB(name);
        LDBDatabase idx = manager.getDB(name+"_idx");

        FireRank rank = new LDBFireRank(meta,db,idx,name);

        for(int i=0;i<10;i++){
            rank.set(String.valueOf(i),i);
        }

        rank.set("8",-100);

        List<Item> max = rank.max((int)rank.size());
        for(Item item : max){
            System.out.println(item );
        }

    }
}
