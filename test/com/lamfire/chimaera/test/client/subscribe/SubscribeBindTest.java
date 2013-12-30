package com.lamfire.chimaera.test.client.subscribe;


import com.lamfire.chimaera.SubscribePublishListener;
import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.client.Subscribe;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-25
 * Time: 上午10:14
 * To change this template use File | Settings | File Templates.
 */
public class SubscribeBindTest implements SubscribePublishListener {
    private static AtomicInteger counter = new AtomicInteger();
    private Subscribe subscribe;
    private String clientId = "001";

    public SubscribeBindTest(){
        ChimaeraCli cli = new ChimaeraCli();
        cli.open("127.0.0.1",8090);
        subscribe =cli.getSubscribe();
    }
    public void bind() {
        subscribe.bind("TEST",clientId,this);
    }

    public static void main(String[] args) {
        SubscribeBindTest test = new SubscribeBindTest();
        test.bind();
    }

    @Override
    public void onMessage(String key, byte[] message) {
        System.out.println( key + ":" + new String(message));
    }
}
