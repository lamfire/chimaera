package com.lamfire.chimaera;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-16
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public interface Bundler {
    /**
     * 绑定，绑定后能收到发布发布的消息
     * @param key
     * @param clientId
     * @param listener
     */
    public void bind(String key, String clientId, OnMessageListener listener);


    /**
     * 取消对消息的绑定，取消绑定后，将不能再接收到发布的消息。
     * @param key
     * @param clientId
     */
    public void unbind(String key, String clientId);
    /**
     * 发布消息，将消息发送给绑定到该服务KEY上的所有人
     * @param key
     * @param clientId
     * @param bytes
     */
    public void publish(String key, String clientId, byte[] bytes);
}
