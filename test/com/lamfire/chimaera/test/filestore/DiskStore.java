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
    private static final String BDB_DIR = "k:/data/chimaera/";
    private static final String NAME = "BDBStore";

    private static FireStore store;


    public synchronized static FireStore getFireStore()throws IOException{
        if(store != null){
            return store;
        }
        ChimaeraOpts opts = new ChimaeraOpts();
        opts.setStoreOnDisk(true);
        opts.setStoreDir(BDB_DIR);
        opts.setEnableTransactions(true);
        store = FireStoreFactory.makeFireStoreWithBDB(NAME,opts);
        return store;
    }
}
