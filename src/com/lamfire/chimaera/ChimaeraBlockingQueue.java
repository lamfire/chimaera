package com.lamfire.chimaera;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.logger.Logger;

/**
 * 阻塞对例
 * User: lamfire
 * Date: 14-1-8
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraBlockingQueue implements FireQueue {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraBlockingQueue.class);
    private FireQueue storeQueue;

    public ChimaeraBlockingQueue(FireQueue queue) {
        this.storeQueue = queue;
    }

    @Override
    public synchronized void push(byte[] value) {
        this.storeQueue.push(value);
        this.notifyAll();
    }

    @Override
    public synchronized byte[] pop() {
        byte[] result = null;
        do{
            try {
                result = this.storeQueue.pop();
                if(result == null){
                    this.wait();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
        } while(result == null);
        return result;
    }

    @Override
    public synchronized byte[] peek() {
        byte[] result = null;
        do{
            try {
                result = this.storeQueue.peek();
                if(result == null){
                    this.wait();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
        } while(result == null);
        return result;
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
