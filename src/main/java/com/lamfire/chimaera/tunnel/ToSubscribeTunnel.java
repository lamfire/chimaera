package com.lamfire.chimaera.tunnel;

import com.lamfire.chimaera.ChimaeraSubscribe;
import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.logger.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-22
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class ToSubscribeTunnel implements OnMessageListener {
    private static final Logger LOGGER = Logger.getLogger(ToSubscribeTunnel.class);
    private String toKey;
    private long received = 0;

    public ToSubscribeTunnel(String toKey){
        this.toKey = toKey;
    }
    @Override
    public void onMessage(String key, byte[] message) {
        ChimaeraSubscribe.getInstance().publish(this.toKey,message);
        received++;
        if(received % 10000 == 0){
            LOGGER.info("["+toKey+"] received messages " + received);
        }
    }
}
