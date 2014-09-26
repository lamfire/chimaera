package com.lamfire.chimaera.test.benchmark;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.test.dbdengine.bdb.BDBStore;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FireIncrementBenchmark extends BDBStore {
    static final Logger logger = Logger.getLogger(FireIncrementBenchmark.class);
    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static TreeSet<Long> times = new TreeSet<Long>();
    static long timeMillisCount = 0;
    static long timeMillisAvg = 0;
    private FireIncrement increment ;


    public FireIncrementBenchmark(FireIncrement testIncrement){
        this.increment  = testIncrement;

        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;
            @Override
            public void run() {
                synchronized (atomic){
                    int val = atomic.get();
                    System.out.println("[COUNTER/S] : " +  (val - pre) +"/s " + increment.size() +"/" +val);
                    pre = val;
                }
            }
        },1,1, TimeUnit.SECONDS);
    }

    private void write(String v){
        try{
            increment.incr(v);
        }   catch(Exception e){
             e.printStackTrace();
            errorAtomic.getAndIncrement();
            errorList.add(v);
        }
    }

    private long read(String val){
        String key = String.valueOf(val);
        long startAt = System.currentTimeMillis();
        try{
            return increment.get(key);
        }   catch (Exception e){
            logger.error("error get (" + val +")",e);
            errorAtomic.getAndIncrement();
            errorList.add(key);
        }finally{
            long usedMillis = System.currentTimeMillis() - startAt;
            timeMillisCount +=  usedMillis;
        }
        return 0;
    }
	
	private static class Writer implements Runnable  {
        FireIncrementBenchmark test;
        public Writer(FireIncrementBenchmark test){
             this.test = test;
        }
		@Override
		public void run() {
			long startAt = System.currentTimeMillis();
			while(true){
                synchronized (atomic){
                atomic.getAndIncrement();
                int val = RandomUtils.nextInt(100);
                test.write(String.valueOf(val));
				}
			}
		}
	};

    private static class Reader implements Runnable{
        FireIncrementBenchmark test;
        public Reader(FireIncrementBenchmark test){
            this.test = test;
        }
        public void run() {
            long startAt = System.currentTimeMillis();
            while(true){
                int i = atomic.getAndIncrement();
                int val = RandomUtils.nextInt(100);
                long v = test.read(String.valueOf(val)) ;
                if(i % 10000 == 0){
                    long timeUsed = System.currentTimeMillis() - startAt;
                    times.add(timeUsed);
                    startAt = System.currentTimeMillis();
                }
            }
        }
    };

    public void startupBenchmarkWrite(int threads){
        for(int i=0;i<threads;i++){
            Threads.startup(new Writer(this));
        }
    }

    public void startupBenchmarkRead(int threads){
        for(int i=0;i<threads;i++){
            Threads.startup(new Reader(this));
        }
    }
}
