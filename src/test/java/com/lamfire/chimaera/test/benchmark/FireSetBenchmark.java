package com.lamfire.chimaera.test.benchmark;

import com.lamfire.logger.Logger;
import com.lamfire.pandora.FireSet;
import com.lamfire.utils.Lists;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FireSetBenchmark {
    static final Logger logger = Logger.getLogger(FireSetBenchmark.class);
    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static TreeSet<Long> times = new TreeSet<Long>();
    static long timeMillisCount = 0;
    static long timeMillisAvg = 0;
    private FireSet set ;


    public FireSetBenchmark(FireSet test){
        this.set  = test;

        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;
            @Override
            public void run() {
                synchronized (atomic){
                    int val = atomic.get();
                    System.out.println("[COUNTER/S] : " +  (val - pre) +"/s " + set.size() +"/" +val);
                    pre = val;
                }
            }
        },1,1, TimeUnit.SECONDS);
    }

    private void put(String v){
        try{
            byte[] bytes = v.getBytes();
            set.add(bytes);
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
            return set.get(val);
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
        FireSetBenchmark test;
        public Writer(FireSetBenchmark test){
             this.test = test;
        }
		@Override
		public void run() {
			long startAt = System.currentTimeMillis();
			while(true){
                synchronized (atomic){
                    atomic.getAndIncrement();
                    int val = RandomUtils.nextInt(1000000);
                    test.put(String.valueOf(val));
				}
			}
		}
	};

    private static class Reader implements Runnable{
        FireSetBenchmark test;
        public Reader(FireSetBenchmark test){
            this.test = test;
        }
        public void run() {
            long startAt = System.currentTimeMillis();
            while(true){
                atomic.getAndIncrement();
                int val = RandomUtils.nextInt(1000000);
                byte[] bytes = test.get(val) ;
                if(val % 10000 == 0){
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
