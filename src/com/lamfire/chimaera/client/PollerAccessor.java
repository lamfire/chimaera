package com.lamfire.chimaera.client;

import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.Poller;
import com.lamfire.chimaera.Subscribe;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.poller.PollerBindCommand;
import com.lamfire.chimaera.command.poller.PollerPublishCommand;
import com.lamfire.chimaera.command.poller.PollerUnbindCommand;
import com.lamfire.chimaera.command.subscribe.SubscribeBindCommand;
import com.lamfire.chimaera.command.subscribe.SubscribePublishCommand;
import com.lamfire.chimaera.command.subscribe.SubscribeUnbindCommand;
import com.lamfire.chimaera.response.EmptyResponse;

/**
 * 消息定阅器
 * User: lamfire
 * Date: 13-11-14
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public class PollerAccessor implements Poller {
    private ChimaeraTransfer transfer;
    private String store = "_POLLER_";

    PollerAccessor(ChimaeraTransfer transfer){
        this.transfer = transfer;
    }

    /**
     * 将该对象绑定到指定的KEY上，绑定后方可收到来自其它发布者的消息。
     * @param key
     * @param clientId
     * @param listener
     */
    public void bind(String key,String clientId,OnMessageListener listener) {
        PollerBindCommand cmd = new PollerBindCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.POLLER_BIND);
        cmd.setClientId(clientId);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.waitResponse();
        transfer.bindSubscribePublishListener(key, listener);
    }


    /**
     * 取消对消息的绑定，取消绑定后，将不能再接收到发布的消息。
     * @param key
     * @param clientId
     */
    public void unbind(String key,String clientId) {
        PollerUnbindCommand cmd = new PollerUnbindCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setClientId(clientId);
        cmd.setCommand(Command.POLLER_UNBIND);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.waitResponse();
        transfer.unbindSubscribePublishListener(key);
    }

    /**
     * 发布消息，将消息发送给绑定到该服务KEY上的所有人
     * @param key
     * @param clientId
     * @param bytes
     */
    public void publish(String key,String clientId,byte[] bytes) {
        PollerPublishCommand cmd = new PollerPublishCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setClientId(clientId);
        cmd.setCommand(Command.POLLER_PUBLISH);
        cmd.setMessage(bytes);
        transfer.sendCommand(cmd);
    }

    /**
     * 发布消息，将消息发送给绑定到该服务KEY上的所有人
     * @param key
     * @param clientId
     * @param bytes
     */
    public void publishSync(String key,String clientId,byte[] bytes) {
        PollerPublishCommand cmd = new PollerPublishCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setClientId(clientId);
        cmd.setFeedback(true);
        cmd.setCommand(Command.POLLER_PUBLISH);
        cmd.setMessage(bytes);
        transfer.sendCommand(cmd,EmptyResponse.class).await();
    }
}
