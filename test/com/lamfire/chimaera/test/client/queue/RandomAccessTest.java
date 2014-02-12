package com.lamfire.chimaera.test.client.queue;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.test.Config;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.RandomUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-2-12
 * Time: 上午10:24
 * To change this template use File | Settings | File Templates.
 */
public class RandomAccessTest {
    static final Logger LOGGER  = Logger.getLogger(RandomAccessTest.class);
    static final ExecutorService readService = Executors.newFixedThreadPool(1);
    static AtomicInteger readFailedCount = new AtomicInteger();
    static AtomicInteger readCount = new AtomicInteger();
    static class ReadTask implements Runnable{
        private String key;
        private int checkVal;
        @Override
        public void run() {
            FireQueue queue = Config.getFireStore().getFireQueue(key);
            byte[] bytes = queue.pop();
            readCount.incrementAndGet();
            if(bytes == null){
                LOGGER.error("POP Error");
                readFailedCount.incrementAndGet();
                return;
            }
            int val = Bytes.toInt(bytes);
            if(val != checkVal){
                LOGGER.error("Value Error:source = " + checkVal + ",pop = " + val);
                readFailedCount.incrementAndGet();
            }

            //LOGGER.info("POP SUCCESSFULLY");
        }
    }

    static void randmodWrite(){
        int i = RandomUtils.nextInt(1000000);
        String key ="TEST_QUEUE_"+i;
        FireQueue queue = Config.getFireStore().getFireQueue(key);
        queue.push(Bytes.toBytes(i));

        ReadTask task = new ReadTask();
        task.key = key;
        task.checkVal = i;
        readService.submit(task);
    }

    public static void main(String[] args) {
        long startAt = System.currentTimeMillis();
        int i=0;
        while(true){
            randmodWrite();
            i++;
            if(i % 1000 == 0){
                System.out.println("[WRITE] "+i +" -> " +(System.currentTimeMillis() - startAt) +"ms,[READ] - > " + readCount.get() +"/" +readFailedCount.get());
                startAt = System.currentTimeMillis();
            }
        }
    }
}
