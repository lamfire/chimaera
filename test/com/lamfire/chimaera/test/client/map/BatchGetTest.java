package com.lamfire.chimaera.test.client.map;

import com.lamfire.chimaera.test.Config;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchGetTest {
    static final Logger logger = Logger.getLogger(BatchGetTest.class);

    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static long timeMillisCount = 0;
    static long timeMillisAvg = 0;
    static TreeSet<Long> times = new TreeSet<Long>();

    private static byte[] get(int val){
        String key = String.valueOf(val);
        long startAt = System.currentTimeMillis();
        try{
             return Config.getFireStore().getFireMap("TEST_MAP").get(key);
        }   catch (Exception e){
            logger.error("error get (" + val +")",e);
            errorAtomic.getAndIncrement();
            errorList.add(key);
        }finally{
            long usedMillis = System.currentTimeMillis() - startAt;
            timeMillisCount +=  usedMillis;
            timeMillisAvg = timeMillisCount / (1+val);
        }
        return null;
    }

	private static Runnable task = new Runnable() {
		public void run() {
			long startAt = System.currentTimeMillis();
            long count = 0;
			while(true){
                int i = atomic.getAndIncrement();
				byte[] bytes = get(i);
				if(count++ % 1000 == 0){
					long timeUsed = System.currentTimeMillis() - startAt;
                    times.add(timeUsed);
					System.out.println("Thread-"+Thread.currentThread().getId()+" Read "+i + " item time millis:" + timeUsed + " ms,error:" + errorAtomic.get() + ",avg_time:" + timeMillisAvg +" max_time:" + times.last());
					startAt = System.currentTimeMillis();
				}
			}
		}
	};

	public static void main(String[] args) {
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
