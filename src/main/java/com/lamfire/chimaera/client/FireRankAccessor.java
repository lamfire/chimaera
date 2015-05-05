package com.lamfire.chimaera.client;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.rank.*;
import com.lamfire.chimaera.response.ClearResponse;
import com.lamfire.chimaera.response.EmptyResponse;
import com.lamfire.chimaera.response.SizeResponse;
import com.lamfire.chimaera.response.rank.RankListResponse;
import com.lamfire.chimaera.response.rank.RankScoreResponse;
import com.lamfire.pandora.FireRank;
import com.lamfire.pandora.Item;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-16
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class FireRankAccessor implements FireRank {
    private ChimaeraTransfer transfer;
    private String key;
    private String store;

    FireRankAccessor(ChimaeraTransfer transfer, String store, String key) {
        this.transfer = transfer;
        this.store = store;
        this.key = key;
    }


    @Override
    public void put(String name) {
        incr(name, 1);
    }

    @Override
    public void incr(String name, long step) {
        RankIncrCommand cmd = new RankIncrCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setStep(step);
        cmd.setCommand(Command.RANK_INCR);
        cmd.setName(name);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.await();
    }

    @Override
    public void set(String name, long count) {
        RankSetCommand cmd = new RankSetCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCount(count);
        cmd.setCommand(Command.RANK_SET);
        cmd.setName(name);
        ResponseFuture<EmptyResponse> future = transfer.sendCommand(cmd, EmptyResponse.class);
        future.await();
    }

    @Override
    public long score(String name) {
        RankScoreCommand cmd = new RankScoreCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.RANK_SCORE);
        cmd.setName(name);
        ResponseFuture<RankScoreResponse> future = transfer.sendCommand(cmd, RankScoreResponse.class);
        return future.getResponse().getCount();
    }

    @Override
    public long remove(String name) {
        RankRemoveCommand cmd = new RankRemoveCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.RANK_REMOVE);
        cmd.setName(name);
        ResponseFuture<RankScoreResponse> future = transfer.sendCommand(cmd, RankScoreResponse.class);
        return future.getResponse().getCount();
    }

    @Override
    public List<Item> max(int size) {
        RankMaxCommand cmd = new RankMaxCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.RANK_MAX);
        cmd.setSize(size);
        ResponseFuture<RankListResponse> future = transfer.sendCommand(cmd, RankListResponse.class);
        return future.getResponse().getItems();
    }

    @Override
    public List<Item> min(int size) {
        RankMinCommand cmd = new RankMinCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.RANK_MIN);
        cmd.setSize(size);
        ResponseFuture<RankListResponse> future = transfer.sendCommand(cmd, RankListResponse.class);
        return future.getResponse().getItems();
    }

    @Override
    public List<Item> maxRange(int from, int size) {
        RankMaxRangeCommand cmd = new RankMaxRangeCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setFrom(from);
        cmd.setCommand(Command.RANK_MAX_RANGE);
        cmd.setSize(size);
        ResponseFuture<RankListResponse> future = transfer.sendCommand(cmd, RankListResponse.class);
        return future.getResponse().getItems();
    }

    @Override
    public List<Item> minRange(int from, int size) {
        RankMinRangeCommand cmd = new RankMinRangeCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setFrom(from);
        cmd.setCommand(Command.RANK_MIN_RANGE);
        cmd.setSize(size);
        ResponseFuture<RankListResponse> future = transfer.sendCommand(cmd, RankListResponse.class);
        return future.getResponse().getItems();
    }

    @Override
    public long size() {
        RankSizeCommand cmd = new RankSizeCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.RANK_SIZE);
        ResponseFuture<SizeResponse> future = transfer.sendCommand(cmd, SizeResponse.class);
        return future.getResponse().getSize();
    }

    @Override
    public void clear() {
        RankClearCommand cmd = new RankClearCommand();
        cmd.setStore(this.store);
        cmd.setKey(key);
        cmd.setCommand(Command.RANK_CLEAR);
        ResponseFuture<ClearResponse> future = transfer.sendCommand(cmd, ClearResponse.class);
        future.await();
    }
}
