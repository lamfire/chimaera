package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.store.*;
import com.lamfire.utils.RandomUtils;

public class ClientTest {

    static ChimaeraCli cli = Config.getChimaeraCli();
    static FireStore store = Config.getFireStore();

    public static void size(){
        System.out.println(store.size());
    }

    public static void incr() throws IllegalAccessException {
        store.remove("INCREMENT_TEST");
        FireIncrement inc = store.getFireIncrement("INCREMENT_TEST");
        long startAt = System.currentTimeMillis();
        int count = 1000;
        int seed = 10;
        for(int i=0;i<count;i++){
            inc.incrGet(seed);
        }

        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" incrGet "+count+" times :" + timeUsed+ " ms");

        long value = inc.get();
        System.out.println("incr get "+count+" times value:" +value);

        //Asserts.assertEquals(value,score * seed);

        startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            inc.decrGet(seed);
        }

        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" decrGet "+count+" times :" + timeUsed+ " ms");

        value = inc.get();
        System.out.println("decr get "+count+" times value:" +value);

        //Asserts.assertEquals(value,score * seed);

        startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            inc.incr(seed);
        }
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" incr "+count+" times :" + timeUsed+ " ms");

        startAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            inc.decr(seed);
        }

        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" decr "+count+" times :" + timeUsed+ " ms");

        System.out.println("final increment value:"+inc.get());
    }

    public static void list() throws Exception {
        FireList list = store.getFireList("TEST_LIST");
        list.clear();
        for(int i=0;i<100;i++){
            list.add((""+i).getBytes());
        }
        System.out.println(list.size());
        System.out.println(new String(list.get(44)));

        list.set(44,("1231231312313").getBytes());

        System.out.println("list.get:"+new String(list.get(44)));

        list.remove(44);

        System.out.println("list.get:"+new String(list.get(44)));

        System.out.println("list.size:"+list.size());

    }

    public static void set() throws IllegalAccessException{
        FireSet set = store.getFireSet("TEST_SET");
        set.clear();
        for(int i=0;i<100;i++){
            set.add((""+i).getBytes());
        }

        for(int i=0;i<100;i++){
            set.add((""+i).getBytes());
        }

        System.out.println("set.size:"+set.size());

        System.out.println("set.exists:"+set.exists("44".getBytes()));


        System.out.println("set.get:"+new String(set.get(44)));
        System.out.println("set.remove:"+new String(set.remove("44".getBytes())));

        System.out.println("set.get:"+new String(set.get(44)));

        System.out.println("set.exists:"+set.exists("44".getBytes()));

        System.out.println("set.size:"+set.size());

        System.out.println("set.get:"+(set.get(98)));
    }

    public static void queue() throws IllegalAccessException{
        FireQueue queue = store.getFireQueue("TEST_QUEUE");
        queue.clear();
        for(int i=0;i<100;i++){
            queue.push(("" + i).getBytes());
        }

        System.out.println("queue.size:"+queue.size());

        for(int i=0;i<100;i++){
            byte[] bytes = queue.pop();
            System.out.println("queue.popLeft:"+new String(bytes));
        }

        System.out.println("queue.size:"+queue.size());
    }

    public static void map() throws IllegalAccessException{
        FireMap map = store.getFireMap("TEST_MAP");
        String field = "TEST1";
        long startAt = System.currentTimeMillis();
        map.put(field, "22222222222222222".getBytes());
        long timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" Put item timeMillis:" + timeUsed+ " ms");

        startAt = System.currentTimeMillis();
        map.put("TEST0", RandomUtils.randomTextWithFixedLength(1024).getBytes());
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" Put item timeMillis:" + timeUsed+ " ms");


        startAt = System.currentTimeMillis();
        byte[] bytes = map.get(field);
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" Get item timeMillis:" + timeUsed + " ms value:" + new String(bytes));

        startAt = System.currentTimeMillis();
        bytes = map.get("TEST0");
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" Get item timeMillis:" + timeUsed + " ms value:" + new String(bytes));

        startAt = System.currentTimeMillis();
        System.out.println("exists:"+map.exists(field));
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" used time millis:" + timeUsed + " ms\n\n");


        startAt = System.currentTimeMillis();
        System.out.println("exists:"+map.exists("TEST2"));
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" used time millis:" + timeUsed + " ms\n\n");

        startAt = System.currentTimeMillis();
        System.out.println("size:"+map.size());
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" used time millis:" + timeUsed + " ms\n\n");

        startAt = System.currentTimeMillis();
        System.out.println("remove:"+field);
        map.remove(field);
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" used time millis:" + timeUsed + " ms\n\n");

        startAt = System.currentTimeMillis();
        System.out.println("size:"+map.size());
        timeUsed = System.currentTimeMillis() - startAt;
        System.out.println("Thread-"+Thread.currentThread().getId()+" used time millis:" + timeUsed + " ms\n\n");
    }

    public static void main(String[] args)throws Exception{
        incr();
        set();
        list();
        queue();
        map();
        cli.close();
    }
}
