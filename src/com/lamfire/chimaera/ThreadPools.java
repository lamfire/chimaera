package com.lamfire.chimaera;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-14
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPools {
    private static final Logger LOGGER = Logger.getLogger(ThreadPools.class);
    private static ThreadPools pools ;

    public synchronized static ThreadPools  get(){
        if(pools == null){
            pools = new ThreadPools();
        }
        return pools;
    }

    private ExecutorService service;

    private ThreadPools() {
        int threads = ChimaeraOpts.get().getThreads();
        String name = "Chimaera Worker";
        this.service = Executors.newFixedThreadPool(threads, Threads.makeThreadFactory(name));
        LOGGER.info("Create thread pool[" +name +"],fixed size = " + threads);
    }

    public void submit(Runnable run){
        this.service.submit(run);
    }

    public ExecutorService getExecutorService(){
        return this.service;
    }

    public void shutdown(){
        this.service.shutdown();
    }
}
