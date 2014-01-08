package com.lamfire.chimaera;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.logger.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-8
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraBlockingQueue implements FireQueue{
    private static final Logger LOGGER = Logger.getLogger(ChimaeraBlockingQueue.class);
    private FireQueue storeQueue;

    public  ChimaeraBlockingQueue(FireQueue queue){
        this.storeQueue = queue;
    }

    @Override
    public synchronized void push(byte[] value) {
        this.storeQueue.push(value);
        this.notifyAll();
    }

    @Override
    public synchronized byte[] pop() {
        byte[] val = this.storeQueue.pop();
        if(val != null){
            return val;
        }
        try{
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("queue was empty,waiting.");
            }
            this.wait();
        }catch (Exception e){}
        return pop();
    }

    @Override
    public synchronized byte[] peek() {
        byte[] val = this.storeQueue.peek();
        if(val != null){
            return val;
        }
        try{
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("queue was empty,waiting.");
            }
            this.wait();
        }catch (Exception e){}
        return peek();
    }

    @Override
    public int size() {
        return this.storeQueue.size();
    }

    @Override
    public void clear() {
       this.storeQueue.clear();
    }
}
