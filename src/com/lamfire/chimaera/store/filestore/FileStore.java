package com.lamfire.chimaera.store.filestore;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-12-18
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
public class FileStore {
    private String file; //文件路径
    private RecordManager recordManager;

    private int maxCacheSize = 100;  //最大数据操作缓存次数，当达到该值时，刷新更改到文件。
    private AtomicInteger cacheCount = new AtomicInteger(); //数据更改次数记录器

    public FileStore(String file)throws IOException{
        this.file = file;
        this.recordManager = RecordManagerFactory.createRecordManager(file);
    }

    public RecordManager getRecordManager(){
        return this.recordManager;
    }

    public void setMaxCacheSize(int maxCacheSize){
        this.maxCacheSize = maxCacheSize;
    }

    /**
     * 检查数据更改缓冲次数，如果达到最大值，则写入到文件
     */
    protected synchronized void cacheOrFlush(){
        if(cacheCount.getAndIncrement() >= maxCacheSize){
            flush();
        }
    }

    public synchronized void flush(){
        try {
            recordManager.commit();
            cacheCount.set(0);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}
