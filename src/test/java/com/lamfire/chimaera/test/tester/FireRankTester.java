package com.lamfire.chimaera.test.tester;


import com.lamfire.pandora.FireRank;
import com.lamfire.pandora.Item;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.RandomUtils;

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

    void puts(){
        long startAt = System.currentTimeMillis();
        int index = 0;
        for(int i=0;i<100000;i++){
            index =  count.getAndIncrement();
            this.rank.put(String.valueOf(index));
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

    public void testSize(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        long startAt = System.currentTimeMillis();
        rank.clear();
        System.out.println("[Starting] test size() ...");
        int size = RandomUtils.nextInt(10000);
        for(int i=0;i<size;i++){
            rank.put(String.valueOf(i));
        }
        Asserts.assertEquals(size,rank.size());
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("[END] test size():  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }

    public void testPut(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test put()");
        rank.clear();

        String name ="TEST_PUT_KEY";
        int count = RandomUtils.nextInt(1000);
        long startAt = System.currentTimeMillis();

        System.out.println("put count : " + count);
        for(int i=0;i<count;i++){
            this.rank.put(name);
        }
        Asserts.assertEquals(count,rank.score(name));
        System.out.println("score : " + rank.score(name));
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("[END] test put():  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }

    public void testPuts(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test batch puts()");
        rank.clear();
        long startAt = System.currentTimeMillis();
        int count = RandomUtils.nextInt(1000);
        for(int i=0;i<count;i++){
            this.rank.put(String.valueOf(i));
        }
        Asserts.assertEquals(count,rank.size());
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("[END] test batch puts():  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }

    public void testIncr(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test incr()");
        String name = "RANK_INCR_NAME";
        this.rank.set(name,0);
        System.out.println("set score to : 0");
        long startAt = System.currentTimeMillis();
        this.rank.incr(name,1);
        System.out.println("incr score : 1");

        this.rank.incr(name,10);
        System.out.println("incr score : 10");
        long score = this.rank.score(name);
        Asserts.assertEquals(score,11);
        System.out.println("get score = " + rank.score(name));

        long timeUsed = System.currentTimeMillis() - startAt;

        System.out.println("[END] test incr("+name+"):"+score+ " time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");

    }

    public void restRemove(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test remove()");
        String name = "RANK_INCR_NAME_REMOVE";
        this.rank.clear();
        long startAt = System.currentTimeMillis();

        int size = RandomUtils.nextInt(10000);
        System.out.println("put name : " + size);
        for(int i=0;i<size;i++){
            rank.put(String.valueOf(i));
        }
        Asserts.assertEquals(size,rank.size());
        System.out.println("puted name : " + rank.size());

        System.out.println("removing names : " + size);
        for(int i=0;i<size;i++){
            rank.remove(String.valueOf(i));
        }
        Asserts.assertEquals(0,rank.size());
        System.out.println("[TEST] size = "+ rank.size());

        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("[END] test remove("+name+"):  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }

    public void testSet(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test set()");
        long startAt = System.currentTimeMillis();
        this.rank.set("TEST", 99);
        System.out.println("set score 99");
        long timeUsed = System.currentTimeMillis() - startAt;

        long score = this.rank.score("TEST");
        Asserts.assertEquals(99, this.rank.score("TEST"));
        System.out.println("get score = " + score);
        System.out.println("[END] test set():  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }

    public void putsRandom(){
        long startAt = System.currentTimeMillis();
        int i=0;
        while(true){
            i = count.getAndIncrement();
            this.rank.put(String.valueOf((int) (Math.random() * 100000)));
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

    public void putsRandom(int maxItems){
        long startAt = System.currentTimeMillis();
        int i=0;
        while(true){
            i = count.getAndIncrement();
            this.rank.put(String.valueOf(RandomUtils.nextInt(maxItems)));
            if(i % 1000 == 0){
                long timeUsed = System.currentTimeMillis() - startAt;
                if(timeUsed > max_used_time){
                    max_used_time =  timeUsed;
                }
                System.out.println("rank.put():"+i+ "pcs,time_millis:"+ timeUsed +" ms,max_time_used:" + max_used_time);
                startAt = System.currentTimeMillis();
            }
            if(i >= maxItems){
                return;
            }
        }
    }

    public void putsRandomFixTimes(int count){
        long startAt = System.currentTimeMillis();
        long begin = startAt;
        for(int i=0;i<count;i++){
            this.rank.put(String.valueOf((int) (Math.random() * count)));
            if(i % 1000 == 0){
                long timeUsed = System.currentTimeMillis() - startAt;
                if(timeUsed > max_used_time){
                    max_used_time =  timeUsed;
                }
                System.out.println("rank.put():"+i+ "pcs,time_millis:" + timeUsed +" ms,max_time_used:" + max_used_time);
                startAt = System.currentTimeMillis();
            }
        }
        System.out.println("rank.put("+count+"):" + (System.currentTimeMillis() - begin) +" ms");
        System.out.println("rank.size:" + this.rank.size());
    }

    public void testMax(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test max()");
        this.rank.clear();
        long startAt = System.currentTimeMillis();

        int size = RandomUtils.nextInt(10000);
        System.out.println("put elements : " + size);
        for(int i=0;i<size;i++){
            rank.put(String.valueOf(i));
        }
        Asserts.assertEquals(size,rank.size());

        rank.put("999999");
        rank.put("999999");
        rank.put("999999");

        rank.put("9999999");
        rank.put("9999999");

        rank.put("99999999");

        List<Item> maxItems = this.rank.max(10);
        print(maxItems);

        Item top0 = maxItems.get(0);
        Asserts.assertEquals(top0.getValue(),3);
        Asserts.assertEquals(top0.getName(),"999999");

        Item top1 = maxItems.get(1);
        Asserts.assertEquals(top1.getValue(),2);
        Asserts.assertEquals(top1.getName(),"9999999");

        Item top2 = maxItems.get(2);
        Asserts.assertEquals(top2.getValue(),1);
        Asserts.assertEquals(top2.getName(),"99999999");

        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("[END] test max():  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }


    public void testMin(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test min()");
        this.rank.clear();
        long startAt = System.currentTimeMillis();

        int size = RandomUtils.nextInt(10000);
        for(int c=0;c<4;c++){
            System.out.println("put elements : " + size);
            for(int i=0;i<size;i++){
                rank.put(String.valueOf(i));
            }
        }
        Asserts.assertEquals(size,rank.size());

        rank.put("999999");
        rank.put("999999");
        rank.put("999999");

        rank.put("9999999");
        rank.put("9999999");

        rank.put("99999999");

        List<Item> items = this.rank.min(10);
        print(items);

        Item top0 = items.get(2);
        Asserts.assertEquals(top0.getValue(),3);
        Asserts.assertEquals(top0.getName(),"999999");

        Item top1 = items.get(1);
        Asserts.assertEquals(top1.getValue(),2);
        Asserts.assertEquals(top1.getName(),"9999999");

        Item top2 = items.get(0);
        Asserts.assertEquals(top2.getValue(),1);
        Asserts.assertEquals(top2.getName(),"99999999");

        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("[END] test min():  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }

    public void testMaxRange(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test maxRange()");
        this.rank.clear();
        long startAt = System.currentTimeMillis();

        int size = RandomUtils.nextInt(10000);
        System.out.println("put elements : " + size);
        for(int i=0;i<size;i++){
            rank.put(String.valueOf(i));
        }
        Asserts.assertEquals(size,rank.size());

        for(int i=0;i<100;i++){
            for(int j=10;j<i;j++){
                rank.put(String.valueOf(j));
            }
        }

        List<Item> items = this.rank.maxRange(0,15);
        print(items);

        int name = 10;
        for(Item item : items){
            Asserts.assertEquals(String.valueOf(name) ,item.getName());
            Asserts.assertEquals(item.getValue() ,100 - name);
            name++;
        }

        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("[END] test maxRange():  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }

    public void testMinRange(){
        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test minRange()");
        this.rank.clear();
        long startAt = System.currentTimeMillis();

        int size = RandomUtils.nextInt(100);
        System.out.println("putting elements : " + size);
        for(int i=0;i<size;i++){
            for(int j=i;j<size;j++){
                rank.put(String.valueOf(j));
            }
        }

        List<Item> items = this.rank.minRange(0,15);
        print(items);

        int name = 0;
        for(Item item : items){
            Asserts.assertEquals(String.valueOf(name) ,item.getName());
            Asserts.assertEquals(item.getValue() ,(name +1));
            name++;
        }

        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("[END] test minRange():  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }


    public void testScore(){

        System.out.println("\n\n\n----------------------------------------------------------------->");
        System.out.println("[Starting] test score()");
        long startAt = System.currentTimeMillis();
        String name = "TEST_SCORE_001";
        long score = RandomUtils.nextInt(1000000);
        this.rank.set(name,score);
        System.out.println("set score :" + score);
        long value = this.rank.score(name);
        System.out.println("score = " + value);
        Asserts.assertEquals(score,value);
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("[END] test minRange():  time_millis:" + timeUsed +" ms");
        System.out.println("<-----------------------------------------------------------------");
    }

    public void clear(){
        long startAt = System.currentTimeMillis();
        this.rank.clear();
        long timeUsed = System.currentTimeMillis() - startAt;
    }

    public void test() {
        long startAt = System.currentTimeMillis();
        System.out.println("==>> startup : " + this.getClass().getName());
        testPut();
        testSet();
        testScore();
        testSize();
        testPuts();
        testIncr();
        testMax();
        testMaxRange();
        testMin();
        testMinRange();
        System.out.println("<<== finish : " + this.getClass().getName() + " - " + (System.currentTimeMillis() - startAt) +"ms");
    }
}
