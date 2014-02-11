package com.lamfire.chimaera.test.client.queue;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.RandomUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-2-11
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
public class MutilQueueWriteTest {
    public static void main(String[] args) {
        long startAt = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            FireQueue queue = Config.getFireStore(args).getFireQueue("TEST_QUEUE_"+i);
            queue.push(Bytes.toBytes(i));
            if(i % 1000 == 0){
                System.out.println("[WRITE] "+i +" -> " +(System.currentTimeMillis() - startAt) +"ms");
                startAt = System.currentTimeMillis();
            }
        }
        Config.shutdown();
    }
}
