package com.lamfire.chimaera.response.subscribe;

import com.lamfire.chimaera.response.Response;

public class PublishResponse extends Response {
    public static final int STATUS = 201;
    public static final byte TYPE_POLLER = 1;
    public static final byte TYPE_SUBSCRIBE = 2;

    public PublishResponse() {
        super.setStatus(STATUS);
    }

    private String key;
    private byte type;
    private byte[] message;

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
