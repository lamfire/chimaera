package com.lamfire.chimaera.test.filestore.benchmark;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.test.filestore.DiskStore;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DiskFireQueueBenchmark extends DiskStore{
    static final Logger logger = Logger.getLogger(DiskFireQueueBenchmark.class);
    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static TreeSet<Long> times = new TreeSet<Long>();
    static long timeMillisCount = 0;
    static long timeMillisAvg = 0;
    private FireQueue queue ;


    public DiskFireQueueBenchmark() throws IOException {
        this.queue  = getFireStore().getFireQueue("TEST_QUEUE_BENCHMARK");

        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;
            @Override
            public void run() {
                int val = atomic.get();
                System.out.println("[COUNTER/S] : " +  (val - pre) +"/s ");
                pre = val;
            }
        },1,1, TimeUnit.SECONDS);
    }

    private void put(String v){
        try{
            byte[] bytes = v.getBytes();
            queue.push(bytes);
        }   catch(Exception e){
             e.printStackTrace();
            errorAtomic.getAndIncrement();
            errorList.add(v);
        }
    }

    private byte[] get(int val){
        String key = String.valueOf(val);
        long startAt = System.currentTimeMillis();
        try{
            return queue.pop();
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
	
	private static class Writer implements Runnable  {
        DiskFireQueueBenchmark test;
        public Writer(DiskFireQueueBenchmark test){
             this.test = test;
        }
		@Override
		public void run() {
			long startAt = System.currentTimeMillis();
			while(true){
                int i = atomic.getAndIncrement();
                test.put(String.valueOf(i));
				if(i % 10000 == 0){
					long timeUsed = System.currentTimeMillis() - startAt;
                    times.add(timeUsed);
					//System.out.println("Thread-"+Thread.currentThread().getId()+" Write "+i + " item time millis:" + timeUsed +" ms,error:" + errorAtomic.get() +" max_time_used:" + times.last());
					startAt = System.currentTimeMillis();
				}
			}
		}
	};

    private static class Reader implements Runnable{
        DiskFireQueueBenchmark test;
        public Reader(DiskFireQueueBenchmark test){
            this.test = test;
        }
        public void run() {
            long startAt = System.currentTimeMillis();
            while(true){
                int i = atomic.getAndIncrement();
                byte[] bytes = test.get(i);
                if(i % 10000 == 0){
                    long timeUsed = System.currentTimeMillis() - startAt;
                    times.add(timeUsed);
                    //System.out.println("Thread-"+Thread.currentThread().getId()+" Read "+i + " item time millis:" + timeUsed + " ms,error:" + errorAtomic.get() + ",avg_time:" + timeMillisAvg +" max_time:" + times.last());
                    startAt = System.currentTimeMillis();
                }
            }
        }
    };


	public static void startupWriteThreads(int threads) throws Exception{
        DiskFireQueueBenchmark test = new DiskFireQueueBenchmark();
        for(int i=0;i<threads;i++){
            Threads.startup(new Writer(test));
        }
    }

    public static void startupReadThreads(int threads) throws Exception{
        DiskFireQueueBenchmark test = new DiskFireQueueBenchmark();
        for(int i=0;i<threads;i++){
            Threads.startup(new Reader(test));
        }
    }
}
