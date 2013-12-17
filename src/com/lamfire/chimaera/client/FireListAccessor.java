package com.lamfire.chimaera.client;

import com.lamfire.chimaera.command.list.*;
import com.lamfire.chimaera.response.SizeResponse;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.EmptyResponse;
import com.lamfire.chimaera.response.GetsResponse;
import com.lamfire.chimaera.store.FireList;
import com.lamfire.chimaera.response.GetResponse;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-12
 * Time: 下午2:53
 * To change this template use File | Settings | File Templates.
 */
public class FireListAccessor implements FireList {
    private ChimaeraTransfer transfer;
    private String key;
    private String store;

    FireListAccessor(ChimaeraTransfer transfer,String store, String key){
        this.transfer = transfer;
        this.store = store;
        this.key = key;
    }

    @Override
    public void add(byte[] value) {
        ListAddCommand cmd = new ListAddCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.LIST_ADD);
        cmd.setValue(value);
        transfer.sendCommand(cmd,EmptyResponse.class).waitResponse();
    }

    @Override
    public void set(int index, byte[] value) {
        ListSetCommand cmd = new ListSetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setIndex(index);
        cmd.setCommand(Command.LIST_SET);
        cmd.setValue(value);
        transfer.sendCommand(cmd,EmptyResponse.class).waitResponse();
    }

    @Override
    public byte[] get(int index) {
        ListGetCommand cmd = new ListGetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setIndex(index);
        cmd.setCommand(Command.LIST_GET);
        ResponseFuture<GetResponse> future = transfer.sendCommand(cmd, GetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public List<byte[]> gets(int fromIndex, int size) {
        ListGetsCommand cmd = new ListGetsCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setFromIndex(fromIndex);
        cmd.setSize(size);
        cmd.setCommand(Command.LIST_GETS);
        ResponseFuture<GetsResponse> future = transfer.sendCommand(cmd, GetsResponse.class);
        return future.getResponse().getValues();
    }

    @Override
    public byte[] remove(int index) {
        ListRemoveCommand cmd = new ListRemoveCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setIndex(index);
        cmd.setCommand(Command.LIST_REMOVE);
        ResponseFuture<GetResponse> future = transfer.sendCommand(cmd, GetResponse.class);
        return future.getResponse().getValue();
    }

    @Override
    public int size() {
        ListSizeCommand cmd = new ListSizeCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.LIST_SIZE);
        ResponseFuture<SizeResponse> future = transfer.sendCommand(cmd, SizeResponse.class);
        return future.getResponse().getSize();
    }

    @Override
    public void clear() {
        ListClearCommand cmd = new ListClearCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.LIST_CLEAR);
        transfer.sendCommand(cmd,EmptyResponse.class).waitResponse();
    }
}
