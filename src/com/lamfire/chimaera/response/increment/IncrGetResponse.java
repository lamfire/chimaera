package com.lamfire.chimaera.response.increment;

import com.lamfire.chimaera.response.Response;

public class IncrGetResponse extends Response {

    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
