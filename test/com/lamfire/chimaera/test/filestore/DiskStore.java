package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.ThreadPools;
import com.lamfire.chimaera.store.filestore.DiskFireStore;
import com.lamfire.thalia.ThaliaDatabase;

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

    private static ThaliaDatabase engine;
    private static DiskFireStore store;
    public synchronized static ThaliaDatabase getThaliaDatabase()throws IOException{
        if(engine != null){
            return engine;
        }
        engine = new ThaliaDatabase(FILE,false,false,false,false,0);
        return engine;
    }

    public synchronized static  ThaliaDatabase getThaliaDatabase(String file)throws IOException{
        if(engine != null){
            return engine;
        }
        engine = new ThaliaDatabase(file,false,false,false,false,0);
        return engine;
    }

    public synchronized static DiskFireStore getDiskFireStore()throws IOException{
        if(store != null){
            return store;
        }
        store = new DiskFireStore(getThaliaDatabase(),NAME);
        return store;
    }
}
