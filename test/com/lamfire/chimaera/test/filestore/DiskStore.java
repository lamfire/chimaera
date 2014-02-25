package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.ThreadPools;
import com.lamfire.chimaera.store.filestore.DiskFireStore;
import com.lamfire.chimaera.store.filestore.StoreEngine;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-24
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
public class DiskStore {
    private static final String FILE = "/data/chimaera/store";
    private static final String NAME = "TESTSTORE1";

    private static StoreEngine engine;
    private static DiskFireStore store;
    public synchronized static  StoreEngine getStoreEngine()throws IOException{
        if(engine != null){
            return engine;
        }
        engine = new StoreEngine(FILE,true,true,0,0,false,0, ThreadPools.get().getScheduledExecutorService());
        return engine;
    }

    public synchronized static DiskFireStore getDiskFireStore()throws IOException{
        if(store != null){
            return store;
        }
        store = new DiskFireStore(getStoreEngine(),NAME);
        return store;
    }
}
