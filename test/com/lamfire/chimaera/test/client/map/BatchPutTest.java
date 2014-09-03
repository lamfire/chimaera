package com.lamfire.chimaera.test.client.map;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchPutTest {

    static AtomicInteger atomic = new AtomicInteger();
    static TreeSet<Long> times = new TreeSet<Long>();

    static{
        Threads.scheduleWithFixedDelay(new Runnable() {
            private int pre = 0;
            @Override
            public void run() {
                try{
                    int val = atomic.get();
                    if(!times.isEmpty()){
                        System.out.println( (val - pre ) + "/s,count="+ val +  ",max_time=" + times.last() +"ms");
                    }else{
                        System.out.println( (val - pre ) + "/s,count="+ val);
                    }
                    pre = val;
                }catch (Throwable t){
                    t.printStackTrace();
                }
            }
        },1,1, TimeUnit.SECONDS);

        System.out.println("monitor starting...");
    }

	private static Runnable task = new Runnable() {
		@Override
		public void run() {
			long startAt = System.currentTimeMillis();
            long count = 0;
            FireMap map = Config.getFireStore().getFireMap("TEST_MAP");
            System.out.println("Thread-"+Thread.currentThread().getId()+" starting...");
			while(true){
                int i = atomic.getAndIncrement();
				String v = String.valueOf(i);
                byte[] bytes = v.getBytes();

                long start = System.currentTimeMillis();
                map.put(v, bytes);
                times.add( System.currentTimeMillis() - start);
			}
		}
	};


	public static void main(String[] args) {
		Threads.startup(task);
//        Threads.startup(task);
//        Threads.startup(task);
//        Threads.startup(task);
//        Threads.startup(task);
//        Threads.startup(task);
//        Threads.startup(task);
//        Threads.startup(task);
//        Threads.startup(task);
//        Threads.startup(task);
	}
}
