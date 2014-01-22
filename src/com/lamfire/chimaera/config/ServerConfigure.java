package com.lamfire.chimaera.config;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-21
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class ServerConfigure {
    private String bind = "0.0.0.0";
    private int port = 19800;
    private boolean storeInMemory = true;
    private String storeDir;
    private int flushThresholdOps = -1;
    private int flushInterval = 60;
    private int threads = 16;
    private boolean enableLocking = false;
    private boolean enableTransactions = false;
    private boolean enableCache = true;

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isStoreInMemory() {
        return storeInMemory;
    }

    public void setStoreInMemory(boolean storeInMemory) {
        this.storeInMemory = storeInMemory;
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

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
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

    public int getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(int flushInterval) {
        this.flushInterval = flushInterval;
    }
}
