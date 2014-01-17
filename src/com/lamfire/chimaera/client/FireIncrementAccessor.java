package com.lamfire.chimaera.client;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.increment.*;
import com.lamfire.chimaera.response.EmptyResponse;
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
    public void incr() {
        incr(1);
    }

    @Override
    public void incr(long step) {
        IncrementIncrCommand cmd = new IncrementIncrCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.INCREMENT_INCR);
        cmd.setStep(step);
        transfer.sendCommand(cmd, EmptyResponse.class).await();
    }

    @Override
    public void decr() {
        decr(1);
    }

    @Override
    public void decr(long step) {
        IncrementDecrCommand cmd = new IncrementDecrCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.INCREMENT_DECR);
        cmd.setStep(step);
        transfer.sendCommand(cmd, EmptyResponse.class).await();
    }

    @Override
    public long get() {
        IncrementGetCommand cmd = new IncrementGetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.INCREMENT_GET);
        ResponseFuture<IncrGetResponse> future = transfer.sendCommand(cmd, IncrGetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public void set(long value) {
        IncrementSetCommand cmd = new IncrementSetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.INCREMENT_SET);
        cmd.setValue(value);
        ResponseFuture<IncrGetResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.await();
    }

    @Override
    public long incrGet() {
        return incrGet(1);
    }

    @Override
    public long incrGet(long step) {
        IncrementIncrGetCommand cmd = new IncrementIncrGetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.INCREMENT_INCR_GET);
        cmd.setStep(step);
        ResponseFuture<IncrGetResponse> future = transfer.sendCommand(cmd, IncrGetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public long decrGet() {
        return decrGet(1);
    }

    @Override
    public long decrGet(long step) {
        IncrementDecrGetCommand cmd = new IncrementDecrGetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.INCREMENT_DECR_GET);
        cmd.setStep(step);
        ResponseFuture<IncrGetResponse> future = transfer.sendCommand(cmd, IncrGetResponse.class);
        return future.getResponse().getValue();
    }
}
