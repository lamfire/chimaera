package com.lamfire.chimaera.drainage;

import com.lamfire.chimaera.ChimaeraSubscribe;
import com.lamfire.chimaera.OnMessageListener;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-22
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class ToSubscribeDrainage implements OnMessageListener {
    private String toKey;

    public ToSubscribeDrainage(String toKey){
        this.toKey = toKey;
    }
    @Override
    public void onMessage(String key, byte[] message) {
        ChimaeraSubscribe.getInstance().publish(this.toKey,message);
    }
}
