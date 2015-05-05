package com.lamfire.chimaera;

import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;
import com.lamfire.pandora.PandoraMaker;
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

    public synchronized static Pandora makePandora(String name, ChimaeraOpts opts)throws IOException{
        Pandora store = null;
        store = make(name, opts);
        return store;
    }


    private synchronized static Pandora make(String name,ChimaeraOpts opts) throws IOException {
        if(!FileUtils.exists(opts.getDataDir())){
            FileUtils.makeDirs(opts.getDataDir());
        }
        String storeDir = FilenameUtils.concat(opts.getDataDir(),name);
        PandoraMaker maker = new PandoraMaker(storeDir,name);
        maker.cacheSize(opts.getCacheSize());
        maker.blockSize(opts.getBlockSize());
        maker.maxOpenFiles(opts.getMaxOpenFiles());
        maker.createIfMissing(true);
        maker.writeBufferSize(opts.getWriteBufferSize());
        Pandora store = maker.make();
        LOGGER.info("MAKE LDB STORE[" + name + "] :" + opts.getDataDir());
        return store;
    }
}
