package com.lamfire.chimaera.test.client.poller;


import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.Poller;
import com.lamfire.chimaera.client.ChimaeraCli;
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
public class PollerBindTest implements OnMessageListener {
    private static AtomicInteger counter = new AtomicInteger();
    private Poller poller;
    private String clientId = RandomUtils.randomText(3,9);

    public PollerBindTest(){
        ChimaeraCli cli = Config.getChimaeraCli();
        poller =cli.getPoller();
    }
    public void bind() {
        poller.bind("TEST", clientId, this);
    }

    public static void main(String[] args) {
        PollerBindTest test = new PollerBindTest();
        test.bind();
    }

    @Override
    public void onMessage(String key, byte[] message) {
        System.out.println( key + ":" + new String(message));
    }
}
