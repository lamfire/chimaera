package com.lamfire.chimaera.test.benchmark;

import com.lamfire.chimaera.store.FireRank;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FireRankBenchmark {
    static final Logger logger = Logger.getLogger(FireRankBenchmark.class);
    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static TreeSet<Long> times = new TreeSet<Long>();
    static long timeMillisCount = 0;
    static long timeMillisAvg = 0;
    private FireRank rank ;


    public FireRankBenchmark(FireRank test){
        this.rank  = test;

        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;
            @Override
            public void run() {
                synchronized (atomic){
                    int val = atomic.get();
                    System.out.println("[COUNTER/S] : " +  (val - pre) +"/s " + rank.size() +"/" +val);
                    pre = val;
                }
            }
        },1,1, TimeUnit.SECONDS);
    }

    private void put(String v){
        try{
            rank.put(v);
        }   catch(Exception e){
             e.printStackTrace();
            errorAtomic.getAndIncrement();
            errorList.add(v);
        }
    }

    private long get(int val){
        String key = String.valueOf(val);
        long startAt = System.currentTimeMillis();
        try{
            return rank.score(key);
        }   catch (Exception e){
            logger.error("error get (" + val +")",e);
            errorAtomic.getAndIncrement();
            errorList.add(key);
        }finally{
            long usedMillis = System.currentTimeMillis() - startAt;
            timeMillisCount +=  usedMillis;
            timeMillisAvg = timeMillisCount / (1+val);
        }
        return -1;
    }
	
	private static class Writer implements Runnable  {
        FireRankBenchmark test;
        public Writer(FireRankBenchmark test){
             this.test = test;
        }
		@Override
		public void run() {
			long startAt = System.currentTimeMillis();
			while(true){
                synchronized (atomic){
                int i = atomic.getAndIncrement();
                test.put(String.valueOf(i));
				}
			}
		}
	};

    private static class Reader implements Runnable{
        FireRankBenchmark test;
        public Reader(FireRankBenchmark test){
            this.test = test;
        }
        public void run() {
            long startAt = System.currentTimeMillis();
            while(true){
                int i = atomic.getAndIncrement();
               long bytes = test.get(i) ;
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
