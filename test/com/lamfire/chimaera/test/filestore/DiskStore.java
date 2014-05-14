package com.lamfire.chimaera.test.filestore;

import com.lamfire.chimaera.ChimaeraOpts;
import com.lamfire.chimaera.FireStoreFactory;
import com.lamfire.chimaera.store.FireStore;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-24
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
public class DiskStore {
    private static final String JDBM_DIR = "/data/chimaera";
    private static final String MAPDB_DIR = "/data/chimaera/MAPDB";
    private static final String NAME = "TESTSTORE1";

    private static FireStore store;


    public synchronized static FireStore getFireStore()throws IOException{
        if(store != null){
            return store;
        }
        ChimaeraOpts opts = new ChimaeraOpts();
        opts.setStoreOnDisk(true);
        opts.setStoreDir(MAPDB_DIR);
        store = FireStoreFactory.makeFireStore(NAME,opts);
        return store;
    }
}
