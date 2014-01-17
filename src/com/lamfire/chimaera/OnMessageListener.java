package com.lamfire.chimaera;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-11-14
 * Time: 上午11:30
 * To change this template use File | Settings | File Templates.
 */
public interface OnMessageListener {
    public void onMessage(String key, byte[] message);
}
