package com.lamfire.chimaera;

import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.store.bdbstore.BDBEngine;
import com.lamfire.chimaera.store.bdbstore.BDBFireStore;
import com.lamfire.chimaera.store.bdbstore.BDBOpts;
import com.lamfire.chimaera.store.bdbstore.BDBStorageException;
import com.lamfire.chimaera.store.leveldbstore.LDBFireStore;
import com.lamfire.chimaera.store.leveldbstore.LevelDB;
import com.lamfire.chimaera.store.memstore.MemoryFireStore;
import com.lamfire.logger.Logger;
import com.lamfire.utils.FileUtils;
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
            store = makeFireStoreWithLDB(name, opts);
        } else {
            store = makeFireStoreWithMemory(name);
        }
        return store;
    }

    public static FireStore makeFireStoreWithMemory(String name){
        return new MemoryFireStore(name);
    }

    public synchronized static FireStore makeFireStoreWithBDB(String name,ChimaeraOpts opts) throws IOException {
        BDBOpts bdbOpts = new BDBOpts();
        bdbOpts.setPath(opts.getStoreDir());
        bdbOpts.setWriteTransactionEnabled(opts.isEnableTransactions());
        bdbOpts.setCacheSize(opts.getCacheSize());
        if(!FileUtils.exists(opts.getStoreDir())){
            FileUtils.makeDirs(opts.getStoreDir());
        }

        BDBEngine engine = null;
        try {
            engine = new BDBEngine(name,bdbOpts);
        } catch (BDBStorageException e) {
            throw new IOException(e);
        }
        BDBFireStore store = new BDBFireStore(engine,name);
        LOGGER.info("MAKE BDB STORE[" + name + "] :" + opts.getStoreDir());
        return store;
    }

    public synchronized static FireStore makeFireStoreWithLDB(String name,ChimaeraOpts opts) throws IOException {
        if(!FileUtils.exists(opts.getStoreDir())){
            FileUtils.makeDirs(opts.getStoreDir());
        }
        String storeDir = FilenameUtils.concat(opts.getStoreDir(),name);
        LDBFireStore store = new LDBFireStore(storeDir,name);
        LOGGER.info("MAKE LDB STORE[" + name + "] :" + opts.getStoreDir());
        return store;
    }
}
