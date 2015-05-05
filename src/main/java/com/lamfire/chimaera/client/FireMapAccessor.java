package com.lamfire.chimaera.client;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.map.*;
import com.lamfire.chimaera.response.*;
import com.lamfire.chimaera.response.map.MapKeysResponse;
import com.lamfire.pandora.FireMap;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-16
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class FireMapAccessor implements FireMap {
    @Override
    public List<String> keys() {
        MapKeysCommand cmd = new MapKeysCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.MAP_KEYS);
        ResponseFuture<MapKeysResponse> future = transfer.sendCommand(cmd, MapKeysResponse.class);
        MapKeysResponse res = future.getResponse();
        return res.getKeys();
    }

    private ChimaeraTransfer transfer;
    private String key;
    private String store;

    FireMapAccessor(ChimaeraTransfer transfer, String store, String key) {
        this.transfer = transfer;
        this.store = store;
        this.key = key;
    }

    @Override
    public void put(String field, byte[] value) {
        MapPutCommand cmd = new MapPutCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.MAP_PUT);
        cmd.setField(field);
        cmd.setValue(value);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.waitResponse();
    }

    @Override
    public byte[] get(String field) {
        MapGetCommand cmd = new MapGetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.MAP_GET);
        cmd.setField(field);
        ResponseFuture<GetResponse> future = transfer.sendCommand(cmd, GetResponse.class);
        GetResponse res = future.getResponse();
        return res.getValue();
    }

    @Override
    public void remove(String field) {
        MapRemoveCommand cmd = new MapRemoveCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.MAP_REMOVE);
        cmd.setField(field);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.waitResponse();
    }

    @Override
    public boolean exists(String field) {
        MapExistsCommand cmd = new MapExistsCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setField(field);
        cmd.setCommand(Command.MAP_EXISTS);
        ResponseFuture<ExistsResponse> future = transfer.sendCommand(cmd, ExistsResponse.class);
        return future.getResponse().isExists();
    }

    @Override
    public long size() {
        MapSizeCommand cmd = new MapSizeCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.MAP_SIZE);
        ResponseFuture<SizeResponse> future = transfer.sendCommand(cmd, SizeResponse.class);
        SizeResponse res = future.waitResponse();
        return res.getSize();
    }

    @Override
    public void clear() {
        MapClearCommand cmd = new MapClearCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.MAP_CLEAR);
        ResponseFuture<ClearResponse> future = transfer.sendCommand(cmd, ClearResponse.class);
        future.waitResponse();
    }
}
