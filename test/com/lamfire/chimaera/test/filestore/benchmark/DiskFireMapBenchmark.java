package com.lamfire.chimaera.test.filestore.benchmark;

import com.lamfire.chimaera.store.FireMap;
import com.lamfire.chimaera.store.filestore.DiskFireMap;
import com.lamfire.chimaera.test.filestore.DiskStore;
import com.lamfire.logger.Logger;
import com.lamfire.thalia.ThaliaDatabase;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
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
        ThaliaDatabase store = getThaliaDatabase();
        this.map  = new DiskFireMap(store,"TEST_MAP");
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
                int i = atomic.getAndIncrement();
                test.put(String.valueOf(i));
				if(++count % 1000 == 0){
					long timeUsed = System.currentTimeMillis() - startAt;
                    times.add(timeUsed);
					System.out.println("Thread-"+Thread.currentThread().getId()+" Write "+i + " item time millis:" + timeUsed +" ms,error:" + errorAtomic.get() +" max_time_used:" + times.last());
					startAt = System.currentTimeMillis();
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
                int i = atomic.getAndIncrement();
                byte[] bytes = test.get(i);
                if(count++ % 1000 == 0){
                    long timeUsed = System.currentTimeMillis() - startAt;
                    times.add(timeUsed);
                    System.out.println("Thread-"+Thread.currentThread().getId()+" Read "+i + " item time millis:" + timeUsed + " ms,error:" + errorAtomic.get() + ",avg_time:" + timeMillisAvg +" max_time:" + times.last());
                    startAt = System.currentTimeMillis();
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
