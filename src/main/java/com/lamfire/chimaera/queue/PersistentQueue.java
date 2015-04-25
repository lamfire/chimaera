package com.lamfire.chimaera.queue;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.filequeue.FileQueue;
import com.lamfire.filequeue.FileQueueBuilder;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-10-13
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class PersistentQueue implements FireQueue {

    private String dir;
    private String name;
    private FileQueue queue;

    public PersistentQueue(String dir, String name){
        this.dir = dir;
        this.name = name;
    }

    public synchronized FileQueue open() throws IOException {
        if(queue == null){
            FileQueueBuilder builder = new FileQueueBuilder();
            queue = builder.dataDir(dir).name(name).build();
        }
        return queue;
    }

    private void checkStatus(){
        if(queue == null){
            throw new RuntimeException("the queue was Not ready");
        }
    }

    @Override
    public void push(byte[] value) {
        checkStatus();
        queue.add(value);
    }

    @Override
    public byte[] pop() {
        checkStatus();
        return queue.poll();
    }

    @Override
    public byte[] peek() {
        checkStatus();
        return queue.peek();
    }

    @Override
    public long size() {
        checkStatus();
        return queue.size();
    }

    @Override
    public void clear() {
        checkStatus();
        queue.clear();
    }

    public synchronized void close() throws IOException {
        if(queue != null){
            queue.close();
            queue = null;
        }
    }
}
