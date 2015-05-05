package com.lamfire.chimaera;

import com.lamfire.chimaera.queue.PersistentQueue;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;
import com.lamfire.utils.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Chimaera {
    private static final Logger LOGGER = Logger.getLogger(Chimaera.class);
    private static final float FREE_MEMORY_THRESHOLD = 0.1f;
    private static final Map<String, Pandora> stores = Maps.newConcurrentMap();
    private static boolean lackOfMemory = false;
    private static  ChimaeraOpts opts;

    static {
        //check memory
        LOGGER.info("starting memory check schemer.");
        ChimaeraThreadPools.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                lackOfMemory = isLackOfMemoryCheck();
            }
        }, 15, 15, TimeUnit.SECONDS);

        //execute full gc
        LOGGER.info("starting FullGC schemer.");
        ChimaeraThreadPools.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                fullGC();
            }
        }, 60, 60, TimeUnit.MINUTES);
    }

    private Chimaera() {

    }

    public static void setChimaeraOpts(ChimaeraOpts opts){
        Chimaera.opts = opts;
    }

    public static ChimaeraOpts getChimaeraOpts(){
        return opts;
    }

    public static final Pandora getPandora(String storeName) {
        Pandora store = stores.get(storeName);
        if (store != null) {
            return store;
        }
        return newPandora(storeName);
    }


    private synchronized static Pandora newPandora(String storeName) {
        Pandora store = stores.get(storeName);
        if (store == null) {
            try{
                store = FireStoreFactory.makePandora(storeName, Chimaera.opts);

                stores.put(storeName, store);
            }catch (IOException e){
                LOGGER.error(e.getMessage(),e);
                throw new RuntimeException(e);
            }
        }
        return store;
    }

    public static final PersistentQueue makePersistentQueue(String name){
        String dir = opts.getDataDir();
        return new PersistentQueue(FilenameUtils.concat(dir,name),name);
    }

    public static long getAvailableHeapMemory() {
        JvmInfo jvm = JvmInfo.getInstance();
        return jvm.getMem().getHeapMax() - jvm.getMem().getHeapUsed();
    }

    public static long getAvailableNonHeapMemory() {
        JvmInfo jvm = JvmInfo.getInstance();
        return jvm.getMem().getNonHeapMax() - jvm.getMem().getNonHeapUsed();
    }

    private static boolean isLackOfMemoryCheck() {
        JvmInfo jvm = JvmInfo.getInstance();
        long max = jvm.getMem().getHeapMax();
        long free = max - jvm.getMem().getHeapUsed();
        long threshold = (long) (max * FREE_MEMORY_THRESHOLD);
        return free < threshold;
    }

    public static boolean isLackOfMemory() {
        return lackOfMemory;
    }

    private static void fullGC(){
        System.gc();
    }
}
