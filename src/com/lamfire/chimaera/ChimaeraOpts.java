package com.lamfire.chimaera;

import com.lamfire.chimaera.config.ChimaeraXmlParser;
import com.lamfire.chimaera.config.ServerConfigure;
import com.lamfire.logger.Logger;
import com.lamfire.utils.DateFormatUtils;
import com.lamfire.utils.FileUtils;
import com.lamfire.utils.FilenameUtils;

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
    private boolean storeOnDisk = false;
    private String storeDir;
    private int flushThresholdOps = -1;
    private int flushInterval = 60;
    private boolean enableLocking = false;
    private boolean enableTransactions = false;
    private boolean enableCache = true;
    private int cacheSize = 100000;

    public boolean isStoreOnDisk() {
        return storeOnDisk;
    }

    public void setStoreOnDisk(boolean storeOnDisk) {
        this.storeOnDisk = storeOnDisk;
    }

    public String getStoreDir() {
        return storeDir;
    }

    public void setStoreDir(String storeDir) {
        this.storeDir = storeDir;
    }

    public int getFlushThresholdOps() {
        return flushThresholdOps;
    }

    public void setFlushThresholdOps(int flushThresholdOps) {
        this.flushThresholdOps = flushThresholdOps;
    }

    public int getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(int flushInterval) {
        this.flushInterval = flushInterval;
    }

    public boolean isEnableLocking() {
        return enableLocking;
    }

    public void setEnableLocking(boolean enableLocking) {
        this.enableLocking = enableLocking;
    }

    public boolean isEnableTransactions() {
        return enableTransactions;
    }

    public void setEnableTransactions(boolean enableTransactions) {
        this.enableTransactions = enableTransactions;
    }

    public boolean isEnableCache() {
        return enableCache;
    }

    public void setEnableCache(boolean enableCache) {
        this.enableCache = enableCache;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
}
