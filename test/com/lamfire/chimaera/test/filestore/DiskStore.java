package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.store.filestore.DiskDatabase;
import com.lamfire.chimaera.store.filestore.DiskFireStore;

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

    private static DiskDatabase engine;
    private static DiskFireStore store;
    public synchronized static DiskDatabase getDatabase()throws IOException{
        if(engine != null){
            return engine;
        }
        engine = new DiskDatabase(FILE,false,false,false,false,0);
        return engine;
    }

    public synchronized static DiskDatabase getDatabase(String file)throws IOException{
        if(engine != null){
            return engine;
        }
        engine = new DiskDatabase(file,false,false,false,false,0);
        return engine;
    }

    public synchronized static DiskFireStore getDiskFireStore()throws IOException{
        if(store != null){
            return store;
        }
        store = new DiskFireStore(getDatabase(),NAME);
        return store;
    }
}
