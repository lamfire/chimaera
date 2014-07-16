package com.lamfire.chimaera.test.filestore.benchmark;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.test.filestore.DiskStore;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DiskFireMapBenchmark extends DiskStore{
    static final Logger logger = Logger.getLogger(DiskFireMapBenchmark.class);
    static AtomicInteger atomic = new AtomicInteger();
    static AtomicInteger errorAtomic =   new AtomicInteger();
    static List<String> errorList = Lists.newArrayList();
    static TreeSet<Long> times = new TreeSet<Long>();
    static long timeMillisCount = 0;
    static long timeMillisAvg = 0;
    private FireMap map ;
    public DiskFireMapBenchmark() throws IOException {
        this.map  = getFireStore().getFireMap("TEST_MAP_BENCHMARK");
        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;
            @Override
            public void run() {
                synchronized (atomic){
                    int val = atomic.get();
                    System.out.println("[COUNTER/S] : " +  (val - pre) +"/s " + map.size() +"/" + val);
                    pre = val;
                }
            }
        },1,1, TimeUnit.SECONDS);
    }

    private void put(String v){
        try{
            byte[] bytes = v.getBytes();
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
        DiskFireMapBenchmark test;
        public Writer(DiskFireMapBenchmark test){
             this.test = test;
        }
		@Override
		public void run() {
			long startAt = System.currentTimeMillis();
            long count = 0;
			while(true){
                synchronized (atomic){
                int i = atomic.getAndIncrement();
                test.put(String.valueOf(i));
                }
			}
		}
	};

    private static class Reader implements Runnable{
        DiskFireMapBenchmark test;
        public Reader(DiskFireMapBenchmark test){
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


	public static void startupWriteThreads(int threads) throws Exception{
        DiskFireMapBenchmark test = new DiskFireMapBenchmark();
        for(int i=0;i<threads;i++){
            Threads.startup(new Writer(test));
        }
    }

    public static void startupReadThreads(int threads) throws Exception{
        DiskFireMapBenchmark test = new DiskFireMapBenchmark();
        for(int i=0;i<threads;i++){
            Threads.startup(new Reader(test));
        }
    }
}
