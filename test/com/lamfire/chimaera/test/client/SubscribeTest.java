package com.lamfire.chimaera.test.client;


import com.lamfire.chimaera.SubscribePublishListener;
import com.lamfire.chimaera.client.Subscribe;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:14
 * To change this template use File | Settings | File Templates.
 */
public class SubscribeTest implements SubscribePublishListener {
    private static AtomicInteger counter = new AtomicInteger();
    private Subscribe subscribe;

    public SubscribeTest(){
        Config.setupByArgs(SubscribeTest.class,null);
        subscribe = Config.cli.getSubscribe();
    }
    public void bind() {
        subscribe.bind("TEST",this);
    }

    public void publish(String message){
        subscribe.publish("TEST",message.getBytes());
    }

    public static void main(String[] args) {
        SubscribeTest test = new SubscribeTest();
        test.bind();

//        while(true){
//            test.publish("linfan["+counter.getAndIncrement()+"]:" + RandomUtils.randomText(100));
//        }
    }

    @Override
    public void onMessage(String key, byte[] message) {
        System.out.println( key + ":" + new String(message));
    }
}
