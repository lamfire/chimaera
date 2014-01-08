package com.lamfire.chimaera.test.client.map;

import com.lamfire.chimaera.test.client.Config;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchPutTest {

    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static TreeSet<Long> times = new TreeSet<Long>();

    private static void put(String v){
        try{
            byte[] bytes = v.getBytes();
            Config.getFireStore().getFireMap("TEST_MAP").put(v,bytes);
        }   catch(Exception e){
             e.printStackTrace();
            errorAtomic.getAndIncrement();
            errorList.add(v);
        }
    }
	
	private static Runnable task = new Runnable() {
		@Override
		public void run() {
			long startAt = System.currentTimeMillis();
            long count = 0;
			while(true){
                int i = atomic.getAndIncrement();
				put(String.valueOf(i));
				if(++count % 1000 == 0){
					long timeUsed = System.currentTimeMillis() - startAt;
                    times.add(timeUsed);
					System.out.println("Thread-"+Thread.currentThread().getId()+" Write "+i + " item time millis:" + timeUsed +" ms,error:" + errorAtomic.get() +" max_time_used:" + times.last());
					startAt = System.currentTimeMillis();
				}
			}
		}
	};


	public static void main(String[] args) {
        Config.setupByArgs(BatchPutTest.class,args);
		Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
	}
}
