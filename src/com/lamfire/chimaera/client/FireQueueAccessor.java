package com.lamfire.chimaera.client;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.queue.*;
import com.lamfire.chimaera.response.EmptyResponse;
import com.lamfire.chimaera.response.GetResponse;
import com.lamfire.chimaera.response.SizeResponse;
import com.lamfire.chimaera.store.FireQueue;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-15
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class FireQueueAccessor implements FireQueue {

    private ChimaeraTransfer transfer;
    private String key;
    private String store;

    FireQueueAccessor(ChimaeraTransfer transfer, String store, String key) {
        this.transfer = transfer;
        this.store = store;
        this.key = key;
    }

    @Override
    public void push(byte[] value) {
        QueuePushCommand cmd = new QueuePushCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.QUEUE_PUSH);
        cmd.setValue(value);
        transfer.sendCommand(cmd, EmptyResponse.class).waitResponse();
    }

    @Override
    public byte[] pop() {
        QueuePopCommand cmd = new QueuePopCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.QUEUE_POP);
        ResponseFuture<GetResponse> future = transfer.sendCommand(cmd, GetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public byte[] peek() {
        QueuePeekCommand cmd = new QueuePeekCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.QUEUE_PEEK);
        ResponseFuture<GetResponse> future = transfer.sendCommand(cmd, GetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public long size() {
        QueueSizeCommand cmd = new QueueSizeCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.QUEUE_SIZE);
        ResponseFuture<SizeResponse> future = transfer.sendCommand(cmd, SizeResponse.class);
        return future.getResponse().getSize();
    }

    @Override
    public void clear() {
        QueueClearCommand cmd = new QueueClearCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.QUEUE_CLEAR);
        transfer.sendCommand(cmd, EmptyResponse.class).waitResponse();
    }
}
