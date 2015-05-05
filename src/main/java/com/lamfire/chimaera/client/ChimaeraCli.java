package com.lamfire.chimaera.client;

import com.lamfire.chimaera.ChimaeraException;
import com.lamfire.chimaera.Poller;
import com.lamfire.chimaera.Subscribe;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

public class ChimaeraCli {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraCli.class);
    private final ResponseWaitQueue waitQueue = new ResponseWaitQueue();
    private ChimaeraTransfer transfer;
    private int maxConnections = 4;
    private int maxThreads = 8;
    private Subscribe subscribe;
    private Poller poller;

    public int getMaxConnections() {
        return this.maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public synchronized void open(String host, int port) {
        if (this.transfer != null) {
            throw new ChimaeraException("The 'ChimaeraCli' already connected to " + transfer.getHost() + ":" + transfer.getPort());
        }
        this.transfer = new ChimaeraTransfer(host, port, maxConnections, waitQueue);
        this.transfer.setKeepaliveConnsWithClient(maxConnections);
        this.transfer.connect();
        LOGGER.info("Trying to connect to " + transfer.getHost() + ":" + transfer.getPort());
    }

    public Pandora getPandora(String storeName) {
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
        BindMonitor.shutdownMonitorThread();
    }

    public boolean isConnected(){
        return this.transfer.hasConnections();
    }

}
