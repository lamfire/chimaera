package com.lamfire.chimaera.response;

public class GetResponse extends Response{

    private byte[] value;

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
