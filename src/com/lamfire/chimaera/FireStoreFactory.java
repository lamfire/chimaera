package com.lamfire.chimaera;

import com.lamfire.chimaera.store.FireStore;

import com.lamfire.chimaera.store.leveldbstore.LDBFireStore;
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
