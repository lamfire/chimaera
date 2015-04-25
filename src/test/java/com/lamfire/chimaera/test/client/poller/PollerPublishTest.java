package com.lamfire.chimaera.test.client.poller;


import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.Poller;
import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.client.PollerAccessor;
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
public class PollerPublishTest implements OnMessageListener {
    private static AtomicInteger counter = new AtomicInteger();
    private Poller poller;
    private String clientId = "001";

    public PollerPublishTest(){
        ChimaeraCli cli = Config.getChimaeraCli();
        poller =cli.getPoller();
        Threads.scheduleWithFixedDelay(new Runnable() {
            int pre = 0;

            @Override
            public void run() {
                synchronized (counter) {
                    int val = counter.get();
                    System.out.println("[COUNTER/S] : " + (val - pre) + "/s " +  " / " + val);
                    pre = val;
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void publish(String message){
        try{
            PollerAccessor accessor = (PollerAccessor)poller;
            accessor.publish("TEST", clientId, message.getBytes());
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    @Override
    public void onMessage(String key, byte[] message) {
        System.out.println( key + ":" + new String(message));
    }


    public static void main(String[] args) {
        PollerPublishTest test = new PollerPublishTest();
        long startAt = System.currentTimeMillis();
        while(true){
            test.publish("POLLER[" + counter.getAndIncrement() + "]:" + RandomUtils.randomText(100));
        }
    }

}
