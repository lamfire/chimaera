package com.lamfire.chimaera.test.client.queue;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.Threads;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-2-26
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
public class QueuePreformanceTest implements Runnable{

    ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(16);
    ScheduledExecutorService scheduler =  Executors.newScheduledThreadPool(1);
    private FireQueue queue;
    private long submitCount = 0;
    private long preOps = 0;

    QueuePreformanceTest(FireQueue queue){
        this.queue = queue;
        scheduler.scheduleWithFixedDelay(this,1,1, TimeUnit.SECONDS);
    }

    void showSize(){
        long size = queue.size();
        System.out.println("[QUEUE_SIZE]" + size +"\t\t\t" +(size - preOps) );
        preOps = size;
    }

    @Override
    public void run() {
        showSize();
    }

    static class WriteTask implements Runnable{
        private FireQueue queue;
        private byte[] bytes;

        public WriteTask(FireQueue queue,byte[] bytes){
            this.queue = queue;
            this.bytes = bytes;
        }

        @Override
        public void run() {
            queue.push(bytes);
        }
    }

    static class ReadTask implements Runnable{
        private FireQueue queue;
        private byte[] bytes;

        public ReadTask(FireQueue queue){
            this.queue = queue;

        }

        @Override
        public void run() {
            this.bytes = queue.pop();
        }
    }


    public void testWrite(){
        while(true){
            for(int i=0;i<50000;i++){
                String data = "data-" +i;
                executor.submit(new WriteTask(this.queue,data.getBytes()));
                submitCount++;
            }
            while(executor.getCompletedTaskCount() < submitCount){
                Threads.sleep(1000);
            }
        }
    }

    public void testRead(){
        while(true){
            for(int i=0;i<50000;i++){
                String data = "data-" +i;
                executor.submit(new ReadTask(this.queue));
                submitCount++;
            }
            while(executor.getCompletedTaskCount() < submitCount){
                Threads.sleep(1000);
            }
        }
    }

    public static void main(String[] args) {
        FireQueue queue = Config.getFireStore(args).getFireQueue("TEST_QUEUE_PREFORMANCE");
        QueuePreformanceTest test = new QueuePreformanceTest(queue);
        test.testWrite();
        //test.testRead();
    }

}
