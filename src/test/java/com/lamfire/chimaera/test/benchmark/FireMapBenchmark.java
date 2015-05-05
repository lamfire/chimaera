package com.lamfire.chimaera.test.benchmark;

import com.lamfire.logger.Logger;
import com.lamfire.pandora.FireMap;
import com.lamfire.utils.Lists;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FireMapBenchmark {
    static final Logger logger = Logger.getLogger(FireMapBenchmark.class);
    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static TreeSet<Long> times = new TreeSet<Long>();
    static long timeMillisCount = 0;
    static long timeMillisAvg = 0;
    private FireMap map ;
    public FireMapBenchmark(FireMap fireMap) {
        this.map  = fireMap;
        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;
            @Override
            public void run() {
                    int val = atomic.get();
                    System.out.println("[COUNTER/S] : " +  (val - pre) +"/s " + map.size());
                    pre = val;

            }
        },1,1, TimeUnit.SECONDS);
    }

    private void put(String v,byte[] bytes){
        try{
            map.put(v,bytes);
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
            return map.get(key);
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
        FireMapBenchmark test;
        public Writer(FireMapBenchmark test){
             this.test = test;
        }
		@Override
		public void run() {
            byte[] bytes = RandomUtils.randomText(1000).getBytes();
			while(true){
                int i = atomic.getAndIncrement();
                test.put(String.valueOf(i),bytes);
			}
		}
	};

    private static class Reader implements Runnable{
        FireMapBenchmark test;
        public Reader(FireMapBenchmark test){
            this.test = test;
        }
        public void run() {
            long startAt = System.currentTimeMillis();
            long count = 0;
            while(true){
                synchronized (atomic){
                int i = atomic.getAndIncrement();
                byte[] bytes = test.get(i);
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
