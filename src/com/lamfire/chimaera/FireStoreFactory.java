package com.lamfire.chimaera;

import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.store.dbmstore.DBMFireStore;
import com.lamfire.chimaera.store.dbmstore.JDBMEngine;
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
public class FireStoreFactory {
    private static final Logger LOGGER = Logger.getLogger(FireStoreFactory.class);

    public synchronized static FireStore makeFireStore(String name,ChimaeraOpts opts)throws IOException{
        FireStore store = null;
        if (opts != null && opts.isStoreOnDisk()) {
            store = makeFireStoreWithJDBM(name, opts);
        } else {
            store = makeFireStoreWithMemory(name);
        }
        return store;
    }

    public static FireStore makeFireStoreWithMemory(String name){
        return new MemoryFireStore(name);
    }

    public static FireStore makeFireStoreWithJDBM(String name,ChimaeraOpts opts) throws IOException {
        String file = FilenameUtils.concat(opts.getStoreDir(), name);
        JDBMEngine engine = new JDBMEngine(file,opts.isEnableLocking(),opts.isEnableTransactions(),false,opts.isEnableCache(),opts.getCacheSize()) ;
        DBMFireStore store = new DBMFireStore(engine,name);
        LOGGER.info("MAKE STORE[" + name + "] :" + file);
        return store;
    }
}
