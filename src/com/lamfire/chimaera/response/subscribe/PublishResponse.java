package com.lamfire.chimaera.response.subscribe;

import com.lamfire.chimaera.response.Response;

public class PublishResponse extends Response {
    public static final int STATUS = 201;

    public PublishResponse() {
        super.setStatus(STATUS);
    }

    private String key;

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

    public void setStatus(int status) {

    }
}
