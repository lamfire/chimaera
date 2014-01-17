package com.lamfire.chimaera.client;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.set.*;
import com.lamfire.chimaera.response.*;
import com.lamfire.chimaera.store.FireSet;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-15
 * Time: 上午9:32
 * To change this template use File | Settings | File Templates.
 */
public class FireSetAccesser implements FireSet {
    private ChimaeraTransfer transfer;
    private String key;
    private String store;

    FireSetAccesser(ChimaeraTransfer transfer, String store, String key) {
        this.transfer = transfer;
        this.store = store;
        this.key = key;
    }


    @Override
    public void add(byte[] value) {
        SetAddCommand cmd = new SetAddCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.SET_ADD);
        cmd.setValue(value);
        transfer.sendCommand(cmd, EmptyResponse.class).waitResponse();
    }

    @Override
    public byte[] remove(int index) {
        SetRemoveCommand cmd = new SetRemoveCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setIndex(index);
        cmd.setCommand(Command.SET_REMOVE);
        ResponseFuture<GetResponse> future = transfer.sendCommand(cmd, GetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public byte[] remove(byte[] value) {
        SetRemoveCommand cmd = new SetRemoveCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setValue(value);
        cmd.setCommand(Command.SET_REMOVE);
        ResponseFuture<GetResponse> future = transfer.sendCommand(cmd, GetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public byte[] get(int index) {
        SetGetCommand cmd = new SetGetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setIndex(index);
        cmd.setCommand(Command.SET_GET);
        ResponseFuture<GetResponse> future = transfer.sendCommand(cmd, GetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        SetGetsCommand cmd = new SetGetsCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setFromIndex(fromIndex);
        cmd.setSize(size);
        cmd.setCommand(Command.SET_GETS);
        ResponseFuture<GetsResponse> future = transfer.sendCommand(cmd, GetsResponse.class);
        return future.getResponse().getValues();
    }

    @Override
    public boolean exists(byte[] bytes) {
        SetExistsCommand cmd = new SetExistsCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setValue(bytes);
        cmd.setCommand(Command.SET_EXISTS);
        ResponseFuture<ExistsResponse> future = transfer.sendCommand(cmd, ExistsResponse.class);
        return future.getResponse().isExists();
    }

    @Override
    public int size() {
        SetSizeCommand cmd = new SetSizeCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.SET_SIZE);
        ResponseFuture<SizeResponse> future = transfer.sendCommand(cmd, SizeResponse.class);
        return future.getResponse().getSize();
    }

    @Override
    public void clear() {
        SetClearCommand cmd = new SetClearCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.SET_CLEAR);
        transfer.sendCommand(cmd, EmptyResponse.class).waitResponse();
    }
}
