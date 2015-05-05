package com.lamfire.chimaera.client;

import com.lamfire.chimaera.command.*;
import com.lamfire.chimaera.response.*;
import com.lamfire.pandora.*;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-17
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public class FireStoreAccessor implements Pandora {
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
    public long count() {
        CountCommand cmd = new CountCommand();
        cmd.setStore(this.store);
        cmd.setCommand(Command.COUNT);
        ResponseFuture<SizeResponse> future = transfer.sendCommand(cmd, SizeResponse.class);
        SizeResponse res = future.waitResponse();
        return res.getSize();
    }

    @Override
    public Set<String> keys() {
        KeysCommand cmd = new KeysCommand();
        cmd.setStore(this.store);
        cmd.setCommand(Command.KEYS);
        ResponseFuture<KeysResponse> future = transfer.sendCommand(cmd,KeysResponse.class);
        KeysResponse res = future.waitResponse();
        return res.getKeys();
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

}
