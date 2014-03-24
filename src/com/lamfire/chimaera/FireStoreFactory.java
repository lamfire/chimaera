package com.lamfire.chimaera;

import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.store.filestore.DiskFireStore;
import com.lamfire.chimaera.store.filestore.StoreEngine;
import com.lamfire.chimaera.store.memstore.MemoryFireStore;
import com.lamfire.logger.Logger;
import com.lamfire.utils.FilenameUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-2-25
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
class FireStoreFactory {
    private static final Logger LOGGER = Logger.getLogger(FireStoreFactory.class);

    public synchronized static FireStore makeFireStore(String name,ChimaeraOpts opts)throws IOException{
        FireStore store = null;
        if (opts != null && opts.isStoreOnDisk()) {
            store = makeDiskFireStore(name,opts);
        } else {
            store = makeMemoryFireStore(name);
        }
        return store;
    }

    public static MemoryFireStore makeMemoryFireStore(String name){
        return new MemoryFireStore(name);
    }

    public static DiskFireStore makeDiskFireStore(String name,ChimaeraOpts opts) throws IOException {
        String file = FilenameUtils.concat(opts.getStoreDir(), name);
        StoreEngine engine = new StoreEngine(file,opts.isEnableLocking(),opts.isEnableTransactions(),opts.getFlushThresholdOps(),opts.getFlushInterval(),opts.isEnableCache(),opts.getCacheSize(),ThreadPools.get().getScheduledExecutorService()) ;
        DiskFireStore store = new DiskFireStore(engine,name);
        LOGGER.info("MAKE STORE[" + name + "] :" + file);
        return store;
    }
}
