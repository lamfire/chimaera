package com.lamfire.chimaera.test.client;

import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.utils.Asserts;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class FireQueueTest {
    static FireQueue queue ;

    public static void testLL() {
        queue = Config.getFireStore().getFireQueue("TEST_QUEUE");
        queue.clear();
        System.out.println("queue.clear()");

        for(int i=0;i<100;i++){
            String val = String.valueOf(i);
            queue.pushLeft(val.getBytes());
            System.out.println("queue.pushLeft("+val+")");
        }

        int size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(100,size);

        for(int i=0;i<100;i++){
            String val = new String(queue.popLeft());
            System.out.println("queue.popLeft():"+val);
            Asserts.assertEquals(val,String.valueOf(99-i));
        }

        size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(0, size);
    }

    public static void testRR() {
        queue = Config.getFireStore().getFireQueue("TEST_QUEUE");
        queue.clear();
        System.out.println("queue.clear()");

        for(int i=0;i<100;i++){
            String val = String.valueOf(i);
            queue.pushRight(val.getBytes());
            System.out.println("queue.pushRight("+val+")");
        }

        int size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(100,size);

        for(int i=0;i<100;i++){
            String val = new String(queue.popRight());
            System.out.println("queue.popRight():"+val);
            Asserts.assertEquals(val,String.valueOf(99-i));
        }

        size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(0, size);
    }

    public static void testRL() {
        queue = Config.getFireStore().getFireQueue("TEST_QUEUE");
        queue.clear();
        System.out.println("queue.clear()");

        for(int i=0;i<100;i++){
            String val = String.valueOf(i);
            queue.pushRight(val.getBytes());
            System.out.println("queue.pushRight("+val+")");
        }

        int size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(100,size);

        for(int i=0;i<100;i++){
            String val = new String(queue.popLeft());
            System.out.println("queue.popLeft():"+val);
            Asserts.assertEquals(val,String.valueOf(i));
        }

        size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(0, size);
    }

    public static void testLR() {
        queue = Config.getFireStore().getFireQueue("TEST_QUEUE");
        queue.clear();
        System.out.println("queue.clear()");

        for(int i=0;i<100;i++){
            String val = String.valueOf(i);
            queue.pushLeft(val.getBytes());
            System.out.println("queue.pushLeft("+val+")");
        }

        int size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(100,size);

        for(int i=0;i<100;i++){
            String val = new String(queue.popRight());
            System.out.println("queue.popRight():"+val);
            Asserts.assertEquals(val,String.valueOf(i));
        }

        size = queue.size();
        System.out.println("queue.size():"+size);
        Asserts.assertEquals(0, size);
    }

    public static void test() {
        testLL();
        testRR();
        testRL();
        testLR();
    }

    public static void main(String[] args) {
        Config.setupByArgs(FireQueueTest.class,args);
        queue = Config.getFireStore().getFireQueue("TEST_QUEUE");
        test();
        queue.popRight();
        Config.shutdown();
    }
}
