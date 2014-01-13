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

    public int getPoolSize(){
        return this.poolSize;
    }

    public void setPoolSize(int poolSize){
        this.poolSize = poolSize;
    }

    public synchronized void open(String host,int port){
        if(this.transfer != null){
            throw new ChimaeraException("The 'ChimaeraCli' already connected to " + transfer.getHost() +":" + transfer.getPort());
        }
        this.transfer = new ChimaeraTransfer(host, port, poolSize,waitQueue);
        this.transfer.setKeepaliveConnsWithClient(poolSize);
        this.transfer.connect();
        LOGGER.info("ChimaeraCli connected to "  + transfer.getHost() +":" + transfer.getPort());
    }

    public FireStore getFireStore(String storeName){
        return new FireStoreAccessor(transfer,storeName);
    }

    public Subscribe getSubscribe(){
        return new SubscribeAccessor(this.transfer);
    }

    public Poller getPoller(){
        return new PollerAccessor(this.transfer);
    }

    public void close(){
        if(this.transfer != null){
            this.transfer.shutdown();
        }
    }

}
