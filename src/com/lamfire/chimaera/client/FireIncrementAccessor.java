package com.lamfire.chimaera.client;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.increment.*;
import com.lamfire.chimaera.response.EmptyResponse;
import com.lamfire.chimaera.response.SizeResponse;
import com.lamfire.chimaera.response.increment.IncrGetResponse;
import com.lamfire.chimaera.store.FireIncrement;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-12
 * Time: 下午2:53
 * To change this template use File | Settings | File Templates.
 */
public class FireIncrementAccessor implements FireIncrement {
    private ChimaeraTransfer transfer;
    private String store;
    private String key;

    FireIncrementAccessor(ChimaeraTransfer transfer, String store, String key) {
        this.transfer = transfer;
        this.store = store;
        this.key = key;
    }

    @Override
    public void incr(String name) {
        incr(name, 1);
    }

    @Override
    public void incr(String name,long step) {
        IncrementIncrCommand cmd = new IncrementIncrCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setName(name);
        cmd.setCommand(Command.INCREMENT_INCR);
        cmd.setStep(step);
        transfer.sendCommand(cmd, EmptyResponse.class).await();
    }

    @Override
    public long get(String name) {
        IncrementGetCommand cmd = new IncrementGetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setName(name);
        cmd.setCommand(Command.INCREMENT_GET);
        ResponseFuture<IncrGetResponse> future = transfer.sendCommand(cmd, IncrGetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public void set(String name,long value) {
        IncrementSetCommand cmd = new IncrementSetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setName(name);
        cmd.setCommand(Command.INCREMENT_SET);
        cmd.setValue(value);
        ResponseFuture<IncrGetResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.await();
    }

    @Override
    public long incrGet(String name) {
        return incrGet(name,1);
    }

    @Override
    public long incrGet(String name,long step) {
        IncrementIncrGetCommand cmd = new IncrementIncrGetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setName(name);
        cmd.setCommand(Command.INCREMENT_INCR_GET);
        cmd.setStep(step);
        ResponseFuture<IncrGetResponse> future = transfer.sendCommand(cmd, IncrGetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public long remove(String name) {
        IncrementRemoveCommand cmd = new IncrementRemoveCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setName(name);
        cmd.setCommand(Command.INCREMENT_REMOVE);
        ResponseFuture<IncrGetResponse> future = transfer.sendCommand(cmd, IncrGetResponse.class);
        return future.getResponse().getValue();
    }


    @Override
    public int size() {
        IncrementRemoveCommand cmd = new IncrementRemoveCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.INCREMENT_SIZE);
        ResponseFuture<SizeResponse> future = transfer.sendCommand(cmd, SizeResponse.class);
        return future.getResponse().getSize();
    }

    @Override
    public void clear() {
        IncrementClearCommand cmd = new IncrementClearCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.INCREMENT_CLEAR);
        ResponseFuture<IncrGetResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.await();
    }
}
