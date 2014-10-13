package com.lamfire.chimaera.test.client.subscribe;


import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.Subscribe;
import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.client.SubscribeAccessor;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:14
 * To change this template use File | Settings | File Templates.
 */
public class SubscribePublishTest implements OnMessageListener {
    private static AtomicInteger counter = new AtomicInteger();
    private Subscribe subscribe;
    private String clientId = "001";

    public SubscribePublishTest(){
        ChimaeraCli cli = Config.getChimaeraCli();
        subscribe =cli.getSubscribe();
        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;

            @Override
            public void run() {
                synchronized (counter) {
                    int val = counter.get();
                    System.out.println("[COUNTER/S] : " + (val - pre) + "/s " + " / " + val);
                    pre = val;
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void publish(String message){
        try{
            SubscribeAccessor sa = (SubscribeAccessor)subscribe;
            sa.publishSync("TEST", clientId, message.getBytes());
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SubscribePublishTest test = new SubscribePublishTest();
        while(true){
            test.publish("SUBSCRIBE[" + counter.getAndIncrement() + "]:" + RandomUtils.randomText(100));
        }
    }

    @Override
    public void onMessage(String key, byte[] message) {
        System.out.println( key + ":" + new String(message));
    }
}
