package com.lamfire.chimaera.client;

import com.lamfire.chimaera.command.*;
import com.lamfire.chimaera.response.ClearResponse;
import com.lamfire.chimaera.response.EmptyResponse;
import com.lamfire.chimaera.response.ExistsResponse;
import com.lamfire.chimaera.response.SizeResponse;
import com.lamfire.chimaera.store.*;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-17
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public class FireStoreAccessor implements FireStore {
    private ChimaeraTransfer transfer;
    private String store;

    FireStoreAccessor(ChimaeraTransfer transfer, String store) {
        this.transfer = transfer;
        this.store = store;
    }

    @Override
    public void remove(String key) {
        RemoveCommand cmd = new RemoveCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.REMOVE);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.waitResponse();
    }

    @Override
    public long size(String key) {
        SizeCommand cmd = new SizeCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.SIZE);
        ResponseFuture<SizeResponse> future = transfer.sendCommand(cmd, SizeResponse.class);
        SizeResponse res = future.waitResponse();
        return res.getSize();
    }

    @Override
    public void clear(String key) {
        ClearCommand cmd = new ClearCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.CLEAR);
        ResponseFuture<ClearResponse> future = transfer.sendCommand(cmd, ClearResponse.class);
        future.waitResponse();
    }

    @Override
    public long size() {
        return size(null);
    }

    @Override
    public void clear() {
        clear(null);
    }

    @Override
    public boolean exists(String key) {
        ExistsCommand cmd = new ExistsCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.EXISTS);
        ResponseFuture<ExistsResponse> future = transfer.sendCommand(cmd, ExistsResponse.class);
        ExistsResponse res = future.waitResponse();
        return res.isExists();
    }

    @Override
    public FireIncrement getFireIncrement(String key) {
        return new FireIncrementAccessor(transfer, this.store, key);
    }

    @Override
    public FireList getFireList(String key) {
        return new FireListAccessor(transfer, this.store, key);
    }

    @Override
    public FireMap getFireMap(String key) {
        return new FireMapAccessor(transfer, this.store, key);
    }

    @Override
    public FireQueue getFireQueue(String key) {
        return new FireQueueAccessor(transfer, this.store, key);
    }

    @Override
    public FireSet getFireSet(String key) {
        return new FireSetAccesser(transfer, this.store, key);
    }

    @Override
    public FireRank getFireRank(String key) {
        return new FireRankAccessor(transfer, this.store, key);
    }

    @Override
    public void defrag() {
        DefragCommand cmd = new DefragCommand();
        cmd.setStore(this.store);
        cmd.setCommand(Command.DEFRAGE);
        transfer.sendCommand(cmd);
    }

}
