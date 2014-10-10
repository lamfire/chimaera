package com.lamfire.chimaera.client;

import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.Poller;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.poller.PollerBindCommand;
import com.lamfire.chimaera.command.poller.PollerPublishCommand;
import com.lamfire.chimaera.command.poller.PollerUnbindCommand;
import com.lamfire.chimaera.response.EmptyResponse;
import com.lamfire.hydra.Session;

/**
 * 消息定阅器
 * User: lamfire
 * Date: 13-11-14
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public class PollerAccessor implements Poller, Rebundleable {
    private ChimaeraTransfer transfer;
    private String store = "_POLLER_";
    private RebundleMonitor monitor;

    PollerAccessor(ChimaeraTransfer transfer) {
        this.transfer = transfer;
        this.monitor = new RebundleMonitor();
    }

    public Command getBindCommand(String key, String clientId) {
        PollerBindCommand cmd = new PollerBindCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.POLLER_BIND);
        cmd.setClientId(clientId);
        return cmd;
    }

    /**
     * 将该对象绑定到指定的KEY上，绑定后方可收到来自其它发布者的消息。
     *
     * @param key
     * @param clientId
     * @param listener
     */
    public void bind(String key, String clientId, OnMessageListener listener) {
        transfer.setPollerMessageListener(key, listener);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(getBindCommand(key, clientId), EmptyResponse.class);
        future.waitResponse();

        //add monitor
        Rebundler bundler = new Rebundler(this);
        bundler.setKey(key);
        bundler.setClientId(clientId);
        bundler.setListener(listener);
        bundler.setSession(future.getSession());
        monitor.add(bundler);
    }

    public Session rebind(String key, String clientId, OnMessageListener listener) {
        transfer.setPollerMessageListener(key, listener);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(getBindCommand(key, clientId), EmptyResponse.class);
        future.waitResponse();
        return future.getSession();
    }


    public Command getUnbindCommand(String key, String clientId) {
        PollerUnbindCommand cmd = new PollerUnbindCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setClientId(clientId);
        cmd.setCommand(Command.POLLER_UNBIND);
        return cmd;
    }

    /**
     * 取消对消息的绑定，取消绑定后，将不能再接收到发布的消息。
     *
     * @param key
     * @param clientId
     */
    public void unbind(String key, String clientId) {
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(getUnbindCommand(key, clientId), EmptyResponse.class);
        future.waitResponse();
        transfer.removePollerMessageListener(key);
        monitor.remove(key, clientId);
    }

    public Command getPublishCommand(String key, String clientId, byte[] bytes, boolean feedback) {
        PollerPublishCommand cmd = new PollerPublishCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setClientId(clientId);
        cmd.setCommand(Command.POLLER_PUBLISH);
        cmd.setMessage(bytes);
        cmd.setFeedback(feedback);
        return cmd;
    }

    /**
     * 发布消息，将消息发送给绑定到该服务KEY上的所有人
     *
     * @param key
     * @param clientId
     * @param bytes
     */
    public void publish(String key, String clientId, byte[] bytes) {
        transfer.sendCommand(getPublishCommand(key, clientId, bytes, false));
    }

    /**
     * 发布消息，将消息发送给绑定到该服务KEY上的所有人
     *
     * @param key
     * @param clientId
     * @param bytes
     */
    public void publishSync(String key, String clientId, byte[] bytes) {
        transfer.sendCommand(getPublishCommand(key, clientId, bytes, true), EmptyResponse.class).await();
    }
}
