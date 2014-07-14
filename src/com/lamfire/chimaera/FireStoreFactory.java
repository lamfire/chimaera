package com.lamfire.chimaera;

import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.store.bdbstore.BDBEngine;
import com.lamfire.chimaera.store.bdbstore.BDBFireStore;
import com.lamfire.chimaera.store.bdbstore.BDBOpts;
import com.lamfire.chimaera.store.bdbstore.BDBStorageException;
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
            store = makeFireStoreWithBDB(name, opts);
        } else {
            store = makeFireStoreWithMemory(name);
        }
        return store;
    }

    public static FireStore makeFireStoreWithMemory(String name){
        return new MemoryFireStore(name);
    }

    public static FireStore makeFireStoreWithBDB(String name,ChimaeraOpts opts) throws IOException {
        String dir = FilenameUtils.concat(opts.getStoreDir(), name);
        BDBOpts bdbOpts = new BDBOpts();
        bdbOpts.setPath(dir);
        FileUtils.makeDirs(dir);

        BDBEngine engine = null;
        try {
            engine = new BDBEngine(name,bdbOpts);
        } catch (BDBStorageException e) {
            throw new IOException(e);
        }
        BDBFireStore store = new BDBFireStore(engine,name);
        LOGGER.info("MAKE BDB STORE[" + name + "] :" + dir);
        return store;
    }
}
