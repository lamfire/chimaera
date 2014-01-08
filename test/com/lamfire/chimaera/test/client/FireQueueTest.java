package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class FireQueueTest {
    FireQueue queue ;

    public FireQueueTest(FireQueue queue){
        this.queue = queue;
    }

    public void test() {
        queue.clear();
        System.out.println("queue.clear()");

        for(int i=0;i<100;i++){
            String val = String.valueOf(i);
            queue.push(val.getBytes());
            System.out.println("queue.push("+val+")");
        }

        int size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(100,size);

        for(int i=0;i<100;i++){
            String val = new String(queue.pop());
            System.out.println("queue.pop():"+val);
            Asserts.assertEquals(val,String.valueOf(i));
        }

        size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(0, size);
    }

    public static void main(String[] args) {
        Config.setupByArgs(FireRankTest.class, args);
        FireQueueTest test = new FireQueueTest(Config.getFireStore().getFireQueue("TEST_QUEUE"));
        test.test();
        Config.shutdown();
    }
}
