package com.lamfire.chimaera;

import com.lamfire.chimaera.config.ChimaeraXmlParser;
import com.lamfire.chimaera.config.ServerConfigure;
import com.lamfire.logger.Logger;
import com.lamfire.utils.FileUtils;

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

    private ChimaeraOpts() {
        try {
            serverConfigure = ChimaeraXmlParser.get().getServerConfigure();
        } catch (Exception e) {
            LOGGER.warn("Parse '" + ChimaeraXmlParser.XML_RESOURCE + "' file failed,use memory store.");
        }
        initOptions();
    }

    private void initOptions() {
        LOGGER.info("bind:" + serverConfigure.getBind());
        LOGGER.info("port:" + serverConfigure.getPort());
        LOGGER.info("store:" + (serverConfigure.isStoreInMemory() ? "memory" : "file"));
        if (!serverConfigure.isStoreInMemory()) {
            LOGGER.info("storeDir:" + serverConfigure.getStoreDir());
            if (!FileUtils.exists(serverConfigure.getStoreDir())) {
                FileUtils.makeDirs(serverConfigure.getStoreDir());
                LOGGER.info("The store dir not found,make " + serverConfigure.getStoreDir());
            }
            LOGGER.info("storeCacheSize:" + serverConfigure.getFlushThresholdOps());
            LOGGER.info("enableLocking:" + serverConfigure.isEnableLocking());
            LOGGER.info("enableSoftCache:" + serverConfigure.isEnableSoftCache());
            LOGGER.info("enableTransactions:" + serverConfigure.isEnableTransactions());
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


    public int getThreads() {
        return serverConfigure.getThreads();
    }

    public boolean isEnableLocking() {
        return serverConfigure.isEnableLocking();
    }

    public boolean isEnableSoftCache() {
        return serverConfigure.isEnableSoftCache();
    }

    public boolean isEnableTransactions() {
        return serverConfigure.isEnableTransactions();
    }
}
