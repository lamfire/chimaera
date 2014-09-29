package com.lamfire.chimaera.test.benchmark;

import com.lamfire.chimaera.store.FireList;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FireListBenchmark {
    static final Logger logger = Logger.getLogger(FireListBenchmark.class);
    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static TreeSet<Long> times = new TreeSet<Long>();
    static long timeMillisCount = 0;
    static long timeMillisAvg = 0;
    private FireList list ;


    public FireListBenchmark(FireList testList){
        this.list  = testList;

        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;
            @Override
            public void run() {
                synchronized (atomic){
                    int val = atomic.get();
                    System.out.println("[COUNTER/S] : " +  (val - pre) +"/s " + list.size() +"/" +val);
                    pre = val;
                }
            }
        },1,1, TimeUnit.SECONDS);
    }

    private void put(String v){
        try{
            byte[] bytes = v.getBytes();
            list.add(bytes);
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
            return list.get(val);
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
        FireListBenchmark test;
        public Writer(FireListBenchmark test){
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
        FireListBenchmark test;
        public Reader(FireListBenchmark test){
            this.test = test;
        }
        public void run() {
            long startAt = System.currentTimeMillis();
            while(true){
                int i = atomic.getAndIncrement();
                byte[] bytes = test.get(i) ;
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
