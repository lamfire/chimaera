package com.lamfire.chimaera.test.tester;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.chimaera.store.Item;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.Asserts;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-18
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class FireRankTester {
    long max_used_time = 0;
    AtomicInteger count = new AtomicInteger();
    FireRank rank;

    public FireRankTester(FireRank rank){
        this.rank = rank;
    }

    void print(List<Item> list){
        for(Item s:list){
            System.out.println(s.toString());
        }
    }

    FireRank rank(){
        return this.rank;
    }

    void puts(){
        long startAt = System.currentTimeMillis();
        int index = 0;
        while(true){
            index =  count.getAndIncrement();
            rank().put(String.valueOf(index));
            if(index % 1000 == 0){
                long timeUsed = System.currentTimeMillis() - startAt;
                if(timeUsed > max_used_time){
                    max_used_time =  timeUsed;
                }
                System.out.println("rank.put()"+index + "pcs,time_millis:" + timeUsed +" ms,max_time_used:" + max_used_time);
                startAt = System.currentTimeMillis();
            }
        }
    }

    void size(){
        long startAt = System.currentTimeMillis();
        long size = rank().size();
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("rank.size()="+size+" time millis:" + timeUsed +" ms");
    }
    void puts(int count){
        long startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            rank().put(String.valueOf(i));
        }
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("rank.put():"+count+ "pcs,time_millis:" + timeUsed +" ms");
    }

    void incr(){
        String name = "RANK_INCR_NAME";
        rank().set(name,0);
        long startAt = System.currentTimeMillis();
        rank().incr(name,1);
        rank().incr(name,10);
        long score = rank().score(name);
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("rank.incr("+name+"):"+score+ " time_millis:" + timeUsed +" ms");
        Asserts.assertEquals(score,11);
    }

    void set(){
        long startAt = System.currentTimeMillis();
        rank().set("TEST", 99);
        long timeUsed = System.currentTimeMillis() - startAt;

        long count = rank().score("TEST");
        System.out.println("rank.set('TEST',99)=" + count + " time millis:" + timeUsed + " ms");
        Asserts.assertEquals(99, rank().score("TEST"));
    }

    void putsRandom(){
        long startAt = System.currentTimeMillis();
        int i=0;
        while(true){
            i = count.getAndIncrement();
            rank().put(String.valueOf((int) (Math.random() * 100000)));
            if(i % 1000 == 0){
                long timeUsed = System.currentTimeMillis() - startAt;
                if(timeUsed > max_used_time){
                    max_used_time =  timeUsed;
                }
                System.out.println("rank.put():"+i+ "pcs,time_millis:"+ timeUsed +" ms,max_time_used:" + max_used_time);
                startAt = System.currentTimeMillis();
            }
        }
    }

    void putsRandom(int count){
        long startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            rank().put(String.valueOf((int) (Math.random() * count)));
            if(i % 1000 == 0){
                long timeUsed = System.currentTimeMillis() - startAt;
                if(timeUsed > max_used_time){
                    max_used_time =  timeUsed;
                }
                System.out.println("rank.put():"+i+ "pcs,time_millis:" + timeUsed +" ms,max_time_used:" + max_used_time);
                startAt = System.currentTimeMillis();
            }
        }
        System.out.println("rank.size:" + rank().size());
    }

    void max(int count){
        long startAt = System.currentTimeMillis();

        List<Item> maxItems = rank().max(count);
        long timeUsed = System.currentTimeMillis() - startAt;
        print(maxItems);
        System.out.println("rank.max("+count + ") time_millis:" + timeUsed +" ms");
    }


    void min(int count){
        long startAt = System.currentTimeMillis();

        List<Item> minItems = rank().min(count);
        long timeUsed = System.currentTimeMillis() - startAt;
        print(minItems);
        System.out.println("rank.min("+count + ") time_millis:" + timeUsed +" ms");
    }

    void maxRange(int i,int count){
        long startAt = System.currentTimeMillis();

        List<Item> maxItems = rank().maxRange(i, count);
        long timeUsed = System.currentTimeMillis() - startAt;
        print(maxItems);
        System.out.println("rank.maxRange("+i+","+count + ") time_millis:" + timeUsed +" ms");
    }

    void minRange(int i,int count){
        long startAt = System.currentTimeMillis();

        List<Item> minItems = rank().minRange(i, count);
        long timeUsed = System.currentTimeMillis() - startAt;
        print(minItems);
        System.out.println("rank.minRange("+i+","+count + ") time_millis:" + timeUsed +" ms");
    }


    void score(String name){
        long startAt = System.currentTimeMillis();

        long count = rank().score(name);
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("rank.score ("+name + ") = ["+count+"] ,time_millis:" + timeUsed +" ms");
    }

    void clear(){
        long startAt = System.currentTimeMillis();
        rank().clear();
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("rank.clear () time_millis:" + timeUsed +" ms");
    }

    public void test() {
        System.out.println("================> clear()");
        clear();
        System.out.println("================> incr()");
        incr();
        System.out.println("================> set()");
        set();
        System.out.println("================> puts(100)");
        puts(100);
        System.out.println("================>  size()");
        size();
        System.out.println("================> score(\"38\")");
        score("38");
        System.out.println("================> max(20)");
        max(20);
        System.out.println("================> min(10)");
        min(10);
        System.out.println("================> maxRange(2,20)");
        maxRange(2,20);
        System.out.println("================>  minRange(2,10)");
        minRange(0,10);
        System.out.println("<<== finish.");
    }
}
