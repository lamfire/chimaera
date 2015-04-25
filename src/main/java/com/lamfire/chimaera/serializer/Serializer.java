package com.lamfire.chimaera.serializer;

import com.lamfire.json.JSON;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-17
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public interface Serializer<T> {
    public byte[] encode(T t);

    public T decode(byte[] bytes, Class<T> type);

    public T decode(JSON json, Class<T> type);
}
