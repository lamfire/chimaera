package com.lamfire.chimaera.test.client.poller;


import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.Poller;
import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.client.PollerAccessor;
import com.lamfire.chimaera.test.Config;
import com.lamfire.utils.RandomUtils;

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
    }

    public void publish(String message){
        PollerAccessor accessor = (PollerAccessor)poller;
        accessor.publishSync("TEST", clientId, message.getBytes());
    }

    @Override
    public void onMessage(String key, byte[] message) {
        System.out.println( key + ":" + new String(message));
    }


    public static void main(String[] args) {
        PollerPublishTest test = new PollerPublishTest();
        while(true){
            test.publish("POLLER[" + counter.getAndIncrement() + "]:" + RandomUtils.randomText(100));
            if (counter.get() % 10000 == 0) {
                System.out.println(counter.get());
            }
            if(counter.get() >= 10){
                //return;
            }
        }
    }

}
