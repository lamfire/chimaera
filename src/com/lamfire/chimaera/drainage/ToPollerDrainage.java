package com.lamfire.chimaera.drainage;

import com.lamfire.chimaera.ChimaeraPoller;
import com.lamfire.chimaera.ChimaeraSubscribe;
import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.Poller;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-22
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class ToPollerDrainage implements OnMessageListener {
    private String toKey;

    public ToPollerDrainage(String toKey){
        this.toKey = toKey;
    }

    @Override
    public void onMessage(String key, byte[] message) {
        ChimaeraPoller.getInstance().publish(this.toKey,message);
    }
}
