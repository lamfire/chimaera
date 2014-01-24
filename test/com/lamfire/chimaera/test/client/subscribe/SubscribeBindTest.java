package com.lamfire.chimaera.test.client.subscribe;


import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.Subscribe;
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
public class SubscribeBindTest implements OnMessageListener {
    private static AtomicInteger counter = new AtomicInteger();
    private Subscribe subscribe;
    private String clientId = RandomUtils.randomText(3, 9);

    public SubscribeBindTest(){
        subscribe = Config.getChimaeraCli().getSubscribe();
    }
    public void bind() {
        subscribe.bind("TEST", clientId, this);
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
