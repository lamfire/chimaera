package com.lamfire.chimaera.test.client.incr;

import com.lamfire.chimaera.test.client.Config;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchIncrTest {
    static final Logger logger = Logger.getLogger(BatchIncrTest.class);
    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static long timeMillisCount = 0;
    static long timeMillisAvg = 0;
    static TreeSet<Long> times = new TreeSet<Long>();


    private static void test(){
        long startAt = System.currentTimeMillis();
        try{
            Config.getFireStore().getFireIncrement("TEST_INC").incrGet();
        }   catch (Exception e){
            logger.error("error incr ( )",e);
            errorAtomic.getAndIncrement();
        }finally{
            long usedMillis = System.currentTimeMillis() - startAt;
            timeMillisCount +=  usedMillis;
            timeMillisAvg = timeMillisCount / (1+atomic.get());
        }
    }

	private static Runnable task = new Runnable() {
		public void run() {
			long startAt = System.currentTimeMillis();
            long count = 0;
			while(true){
                int i = atomic.getAndIncrement();
				test();
				if(count++ % 1000 == 0){
					long timeUsed = System.currentTimeMillis() - startAt;
                    times.add(timeUsed);
					System.out.println("Thread-"+Thread.currentThread().getId()+" Incr "+i + " item time millis:" + timeUsed + " ms,error:" + errorAtomic.get() + ",avg_time:" + timeMillisAvg +" max_time:" + times.last());
					startAt = System.currentTimeMillis();
				}
			}
		}
	};

	public static void main(String[] args) {
        Config.setupByArgs(BatchIncrTest.class,args);
		Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
        Threads.startup(task);
	}
}
