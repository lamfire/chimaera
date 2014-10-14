package com.lamfire.chimaera;

import com.lamfire.logger.Logger;

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
    private String dataDir;
    private int cacheSize = 8*1024*1024;
    private int maxOpenFiles = 100;
    private int blockSize = 1024 * 1024;
    private int writeBufferSize = 8 *1024 *1024;

    public boolean isStoreOnDisk() {
        return storeOnDisk;
    }

    public void setStoreOnDisk(boolean storeOnDisk) {
        this.storeOnDisk = storeOnDisk;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public int getMaxOpenFiles() {
        return maxOpenFiles;
    }

    public void setMaxOpenFiles(int maxOpenFiles) {
        this.maxOpenFiles = maxOpenFiles;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getWriteBufferSize() {
        return writeBufferSize;
    }

    public void setWriteBufferSize(int writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
    }
}
