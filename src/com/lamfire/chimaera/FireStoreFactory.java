package com.lamfire.chimaera;

import com.lamfire.chimaera.store.FireStore;

import com.lamfire.chimaera.store.leveldbstore.LDBFireStore;
import com.lamfire.chimaera.store.memstore.MemoryFireStore;
import com.lamfire.logger.Logger;
import com.lamfire.utils.FileUtils;
import com.lamfire.utils.FilenameUtils;
import org.iq80.leveldb.Options;

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
        if(!FileUtils.exists(opts.getDataDir())){
            FileUtils.makeDirs(opts.getDataDir());
        }
        String storeDir = FilenameUtils.concat(opts.getDataDir(),name);
        Options options = new Options();
        options.cacheSize(opts.getCacheSize());
        options.blockSize(opts.getBlockSize());
        options.maxOpenFiles(opts.getMaxOpenFiles());
        options.createIfMissing(true);
        options.writeBufferSize(opts.getWriteBufferSize());
        LDBFireStore store = new LDBFireStore(storeDir,name,options);
        LOGGER.info("MAKE LDB STORE[" + name + "] :" + opts.getDataDir());
        return store;
    }
}
