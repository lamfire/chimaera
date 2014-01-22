package com.lamfire.chimaera.client;

import com.lamfire.chimaera.ChimaeraException;
import com.lamfire.chimaera.Poller;
import com.lamfire.chimaera.Subscribe;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.logger.Logger;

public class ChimaeraCli {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraCli.class);
    private final ResponseWaitQueue waitQueue = new ResponseWaitQueue();
    private ChimaeraTransfer transfer;
    private int poolSize = 4;
    private Subscribe subscribe;
    private Poller poller;

    public int getPoolSize() {
        return this.poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public synchronized void open(String host, int port) {
        if (this.transfer != null) {
            throw new ChimaeraException("The 'ChimaeraCli' already connected to " + transfer.getHost() + ":" + transfer.getPort());
        }
        this.transfer = new ChimaeraTransfer(host, port, poolSize, waitQueue);
        this.transfer.setKeepaliveConnsWithClient(poolSize);
        this.transfer.connect();
        LOGGER.info("ChimaeraCli connected to " + transfer.getHost() + ":" + transfer.getPort());
    }

    public FireStore getFireStore(String storeName) {
        return new FireStoreAccessor(transfer, storeName);
    }

    public synchronized Subscribe getSubscribe() {
        if (subscribe == null) {
            this.subscribe = new SubscribeAccessor(this.transfer);
        }
        return subscribe;
    }

    public synchronized Poller getPoller() {
        if (poller == null) {
            this.poller = new PollerAccessor(this.transfer);
        }
        return poller;
    }

    public void close() {
        if (this.transfer != null) {
            this.transfer.shutdown();
        }
        RebundleMonitor.shutdownMonitorThread();
    }

    public boolean isConnected(){
        return this.transfer.hasConnections();
    }

}
