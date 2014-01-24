package com.lamfire.chimaera;

import com.lamfire.chimaera.config.ChimaeraXmlParser;
import com.lamfire.chimaera.config.ServerConfigure;
import com.lamfire.logger.Logger;
import com.lamfire.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-3
 * Time: 下午6:07
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraOpts {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraOpts.class);
    private ServerConfigure serverConfigure;

    static ChimaeraOpts instance;

    public synchronized static ChimaeraOpts get() {
        if (instance == null) {
            instance = new ChimaeraOpts();
        }
        return instance;
    }

    private void cleanStoreDir(String dir) throws IOException {
        FileUtils.cleanDirectory(new File(dir));
    }

    private ChimaeraOpts() {
        try {
            serverConfigure = ChimaeraXmlParser.get().getServerConfigure();
            if((!serverConfigure.isStoreInMemory()) && serverConfigure.isRenew()){
                String storeDir = serverConfigure.getStoreDir();
                LOGGER.info("Configure store in file,setting 'renew' is true,clean store directory : " +storeDir );
                cleanStoreDir(storeDir);
            }
        } catch (Exception e) {
            LOGGER.warn("Parse '" + ChimaeraXmlParser.XML_RESOURCE + "' file failed,use memory store.");
        }
        printOptions();
    }

    private void printOptions() {
        LOGGER.info("bind:" + serverConfigure.getBind());
        LOGGER.info("port:" + serverConfigure.getPort());
        LOGGER.info("store:" + (serverConfigure.isStoreInMemory() ? "memory" : "file"));
        if (!serverConfigure.isStoreInMemory()) {
            LOGGER.info("StoreDir:" + serverConfigure.getStoreDir());
            if (!FileUtils.exists(serverConfigure.getStoreDir())) {
                FileUtils.makeDirs(serverConfigure.getStoreDir());
                LOGGER.info("The store dir not found,make " + serverConfigure.getStoreDir());
            }
            LOGGER.info("FlushThresholdOps:" + serverConfigure.getFlushThresholdOps());
            LOGGER.info("FlushInterval:" + serverConfigure.getFlushInterval());
            LOGGER.info("EnableLocking:" + serverConfigure.isEnableLocking());
            LOGGER.info("EnableCache:" + serverConfigure.isEnableCache());
            LOGGER.info("CacheSize:" + serverConfigure.getCacheSize());
            LOGGER.info("EnableTransactions:" + serverConfigure.isEnableTransactions());
        }
    }

    public String getBind() {
        return serverConfigure.getBind();
    }


    public int getPort() {
        return serverConfigure.getPort();
    }


    public boolean isStoreInMemory() {
        return serverConfigure.isStoreInMemory();
    }


    public String getStoreDir() {
        return serverConfigure.getStoreDir();
    }


    public int getFlushThresholdOps() {
        return serverConfigure.getFlushThresholdOps();
    }

    public int getFlushInterval(){
        return serverConfigure.getFlushInterval();
    }

    public int getCacheSize(){
        return serverConfigure.getCacheSize();
    }

    public int getThreads() {
        return serverConfigure.getThreads();
    }

    public boolean isEnableLocking() {
        return serverConfigure.isEnableLocking();
    }

    public boolean isEnableCache() {
        return serverConfigure.isEnableCache();
    }

    public boolean isEnableTransactions() {
        return serverConfigure.isEnableTransactions();
    }
}
