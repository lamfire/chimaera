package com.lamfire.chimaera;

import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.store.filestore.DiskFireStore;
import com.lamfire.chimaera.store.memstore.MemoryFireStore;
import com.lamfire.logger.Logger;
import com.lamfire.utils.*;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Chimaera {
    private static final Logger LOGGER = Logger.getLogger(Chimaera.class);
    private static final float FREE_MEMORY_THRESHOLD = 0.2f;
    private static final Map<String, FireStore> stores = Maps.newConcurrentMap();
    private static ScheduledExecutorService memoryChecker = Executors.newSingleThreadScheduledExecutor(new ThreadFactory("Chimaera.memory.checker"));
    private static boolean lackOfMemory = false;

    static {
        memoryChecker.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                lackOfMemory = isLackOfMemoryCheck();
            }
        }, 15, 15, TimeUnit.SECONDS);
    }

    private Chimaera() {

    }

    public static final FireStore getFireStore(String storeName) {
        FireStore store = stores.get(storeName);
        if (store != null) {
            return store;
        }
        return newFireStore(storeName,false);
    }

    public static final FireStore getFireStore(String storeName,boolean deleteFilesAfterClose) {
        FireStore store = stores.get(storeName);
        if (store != null) {
            return store;
        }
        return newFireStore(storeName,deleteFilesAfterClose);
    }

    private synchronized static FireStore newFireStore(String storeName,boolean deleteFilesAfterClose) {
        FireStore store = stores.get(storeName);
        if (store == null) {
            ChimaeraOpts opts = ChimaeraOpts.get();
            if (opts.isStoreInMemory()) {
                store = new MemoryFireStore(storeName);
            } else {
                String file = FilenameUtils.concat(opts.getStoreDir(), storeName);
                store = new DiskFireStore(file, storeName,opts.isEnableLocking(),opts.isEnableTransactions(),deleteFilesAfterClose,opts.getFlushThresholdOps(),opts.getFlushInterval(),opts.isEnableCache(),opts.getCacheSize());
                LOGGER.info("MAKE STORE[" + storeName + "] :" + file);
            }
            stores.put(storeName, store);
        }
        return store;
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
}
