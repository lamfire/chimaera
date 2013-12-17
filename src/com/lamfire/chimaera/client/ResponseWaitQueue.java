package com.lamfire.chimaera.client;

import java.util.concurrent.ConcurrentHashMap;

import com.lamfire.logger.Logger;

public class ResponseWaitQueue {
    static final Logger LOGGER = Logger.getLogger(ResponseWaitQueue.class);
	private static final long serialVersionUID = -568333872142979774L;
    private final ConcurrentHashMap<Integer, ResponseFuture> queue = new ConcurrentHashMap<Integer, ResponseFuture>();

    public void add(int id,ResponseFuture<?> future){
        queue.put(id,future);
    }

    public ResponseFuture<?> get(int id){
        return queue.get(id);
    }

    public ResponseFuture<?> remove(int id){
        return queue.remove(id);
    }

    public int size(){
        return queue.size();
    }
}
