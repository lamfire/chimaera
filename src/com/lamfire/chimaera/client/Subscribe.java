package com.lamfire.chimaera.client;

import com.lamfire.chimaera.SubscribePublishListener;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.subscribe.SubscribeBindCommand;
import com.lamfire.chimaera.command.subscribe.SubscribePublishCommand;
import com.lamfire.chimaera.command.subscribe.SubscribeUnbindCommand;
import com.lamfire.chimaera.response.EmptyResponse;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-11-14
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public class Subscribe {
    private ChimaeraTransfer transfer;
    private String store = "_SUBSCRIBE_";

    Subscribe(ChimaeraTransfer transfer){
        this.transfer = transfer;
    }

    public void bind(String key,SubscribePublishListener listener) {
        SubscribeBindCommand cmd = new SubscribeBindCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.SUBSCRIBE_BIND);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.waitResponse();
        transfer.bindSubscribePublishListener(key, listener);
    }


    public void unbind(String key) {
        SubscribeUnbindCommand cmd = new SubscribeUnbindCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.SUBSCRIBE_UNBIND);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.waitResponse();
        transfer.unbindSubscribePublishListener(key);
    }

    public void publish(String key,byte[] bytes) {
        SubscribePublishCommand cmd = new SubscribePublishCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.SUBSCRIBE_PUBLISH);
        cmd.setMessage(bytes);
        transfer.sendCommand(cmd,EmptyResponse.class).await();
    }
}
