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

    public synchronized static ChimaeraThreadPools get() {
        if (pools == null) {
            pools = new ChimaeraThreadPools();
        }
        return pools;
    }

    private ExecutorService service;

    private ChimaeraThreadPools() {
        int threads = -1;
        try {
            threads = 4;
        } catch (Exception e) {

        }
        String name = "Chimaera Worker";
        if (threads > 0) {
            this.service = Executors.newFixedThreadPool(threads, Threads.makeThreadFactory(name));
            LOGGER.info("Create thread pool[" + name + "],fixed thread pool,size = " + threads);
        } else {
            this.service = Executors.newCachedThreadPool(Threads.makeThreadFactory(name));
            LOGGER.info("Create thread pool[" + name + "],cached thread pool.");
        }
    }

    public void submit(Runnable run) {
        this.service.submit(run);
    }

    public ExecutorService getExecutorService() {
        return this.service;
    }

    public ScheduledExecutorService getScheduledExecutorService(){
        return chimaeraScheduler;
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay,  long delay, TimeUnit unit){
        return  chimaeraScheduler.scheduleWithFixedDelay(command,initialDelay,delay,unit);
    }

}
