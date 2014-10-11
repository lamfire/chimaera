package com.lamfire.chimaera.test.store;

import com.lamfire.chimaera.store.*;
import com.lamfire.chimaera.store.leveldbstore.*;
import com.lamfire.chimaera.test.tester.*;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-9-29
 * Time: 上午11:55
 * To change this template use File | Settings | File Templates.
 */
public class LDBFireStoreTest {

    public static void testIncrement(FireStore store) {
        FireIncrement increment = store.getFireIncrement("increment_tester");
        FireIncrementTester tester = new FireIncrementTester(increment);
        tester.test();
    }

    public static void testList(FireStore store) {
        FireList list = store.getFireList("list_tester");
        FireListTester tester = new FireListTester(list);
        tester.test();
    }

    public static void testMap(FireStore store) {
        FireMap map = store.getFireMap("map_tester");
        FireMapTester tester = new FireMapTester(map);
        tester.test();
    }

    public static void testQueue(FireStore store) {
        FireQueue queue =store.getFireQueue("queue_tester");
        FireQueueTester tester = new FireQueueTester(queue);
        tester.test();
    }

    public static void testRank(FireStore store) {
        FireRank set = store.getFireRank("rank_tester");
        FireRankTester tester = new FireRankTester(set);
        tester.test();
    }

    public static void testSet(FireStore store) {
        FireSet set =store.getFireSet("set_tester");
        FireSetTester tester = new FireSetTester(set);
        tester.test();
    }

    public static void testAll(FireStore store){
        testIncrement(store);
        testList(store);
        testMap(store);
        testQueue(store);
        testRank(store);
        testSet(store);
    }

    public static void main(String[] args){
        LDBFireStore store = new LDBFireStore("/data/LevelDB_STORE1","TEST1");
        testAll(store);
    }
}
