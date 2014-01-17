package com.lamfire.chimaera.response;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.increment.IncrGetResponse;
import com.lamfire.chimaera.response.map.MapKeysResponse;
import com.lamfire.chimaera.response.rank.RankListResponse;
import com.lamfire.chimaera.response.rank.RankScoreResponse;
import com.lamfire.chimaera.store.Item;

import java.util.List;

public class Responses {

    private static void fillResponse(Command command, Response res) {
        res.setStore(command.getStore());
        res.setCommand(command.getCommand());
        res.setKey(command.getKey());
    }

    public static ErrorResponse makeErrorResponse(Command command, String error) {
        ErrorResponse res = new ErrorResponse();
        res.setError(error);
        fillResponse(command, res);
        return res;
    }


    public static EmptyResponse makeEmptyResponse(Command command) {
        EmptyResponse res = new EmptyResponse();
        fillResponse(command, res);
        return res;
    }

    public static GetResponse makeGetResponse(Command command, byte[] bytes) {
        GetResponse res = new GetResponse();
        res.setValue(bytes);
        fillResponse(command, res);
        return res;
    }

    public static MapKeysResponse makeMapKeysResponse(Command command, List<String> keys) {
        MapKeysResponse res = new MapKeysResponse();
        res.setKeys(keys);
        fillResponse(command, res);
        return res;
    }

    public static ClearResponse makeClearResponse(Command command) {
        ClearResponse res = new ClearResponse();
        fillResponse(command, res);
        return res;
    }

    public static ExistsResponse makeExistsResponse(Command command, boolean exists) {
        ExistsResponse res = new ExistsResponse();
        res.setExists(exists);
        fillResponse(command, res);
        return res;
    }

    public static PutResponse makePutResponse(Command command) {
        PutResponse res = new PutResponse();
        fillResponse(command, res);
        return res;
    }

    public static SizeResponse makeSizeResponse(Command command, int size) {
        SizeResponse res = new SizeResponse();
        res.setSize(size);
        fillResponse(command, res);
        return res;
    }

    public static IncrGetResponse makeIncrGetResponse(Command command, long value) {
        IncrGetResponse res = new IncrGetResponse();
        res.setValue(value);
        fillResponse(command, res);
        return res;
    }

    public static RankScoreResponse makeCountResponseForCounter(Command command, String name, long count) {
        RankScoreResponse res = new RankScoreResponse();
        res.setName(name);
        res.setCount(count);
        fillResponse(command, res);
        return res;
    }

    public static RankListResponse makeListResponseForCounter(Command command, List<Item> items) {
        RankListResponse res = new RankListResponse();
        res.setItems(items);
        fillResponse(command, res);
        return res;
    }

    public static Response makeGetsResponse(Command command, List<byte[]> list) {
        GetsResponse res = new GetsResponse();
        res.setValues(list);
        fillResponse(command, res);
        return res;
    }
}
