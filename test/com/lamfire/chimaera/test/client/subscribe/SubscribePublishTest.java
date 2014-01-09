package com.lamfire.chimaera.test.client.subscribe;


import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.Subscribe;
import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.client.SubscribeAccessor;
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
public class SubscribePublishTest implements OnMessageListener {
    private static AtomicInteger counter = new AtomicInteger();
    private Subscribe subscribe;
    private String clientId = "001";

    public SubscribePublishTest(){
        ChimaeraCli cli = Config.getChimaeraCli();
        subscribe =cli.getSubscribe();
    }

    public void publish(String message){
        SubscribeAccessor sa = (SubscribeAccessor)subscribe;
        sa.publishSync("TEST", clientId, message.getBytes());
    }

    public static void main(String[] args) {
        SubscribePublishTest test = new SubscribePublishTest();
        while(true){
            test.publish("linfan[" + counter.getAndIncrement() + "]:" + RandomUtils.randomText(100));
            if(counter.get() >= 10){
                //return ;
            }
        }
    }

    @Override
    public void onMessage(String key, byte[] message) {
        System.out.println( key + ":" + new String(message));
    }
}
