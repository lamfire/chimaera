package com.lamfire.chimaera;

import com.lamfire.logger.Logger;
import com.lamfire.utils.ThreadFactory;
import com.lamfire.utils.Threads;

import java.util.concurrent.*;

/**
 * 线程池
 * User: lamfire
 * Date: 14-1-14
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraThreadPools {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraThreadPools.class);
    private static ChimaeraThreadPools pools;
    private static final ScheduledExecutorService chimaeraScheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory("Chimaera.Scheduler"));

    private ExecutorService service;
    private int threads = 4;

    ChimaeraThreadPools(int threads) {
        this.threads = threads;
    }

    private ExecutorService make(int threads) {
        ExecutorService executor;
        String name = "Chimaera Worker";
        if (threads > 0) {
            executor = Executors.newFixedThreadPool(threads, Threads.makeThreadFactory(name));
            LOGGER.info("Create thread pool[" + name + "],fixed thread pool,size = " + threads);
        } else {
            executor = Executors.newCachedThreadPool(Threads.makeThreadFactory(name));
            LOGGER.info("Create thread pool[" + name + "],cached thread pool.");
        }
        return executor;
    }

    public void submit(Runnable run) {
        getExecutorService().submit(run);
    }

    public synchronized ExecutorService getExecutorService() {
        if(this.service == null){
            this.service = make(threads);
        }
        return this.service;
    }

    public static ScheduledExecutorService getScheduledExecutorService(){
        return chimaeraScheduler;
    }

    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay,  long delay, TimeUnit unit){
        return  chimaeraScheduler.scheduleWithFixedDelay(command,initialDelay,delay,unit);
    }

}
