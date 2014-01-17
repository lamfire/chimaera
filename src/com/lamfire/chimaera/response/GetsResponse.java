package com.lamfire.chimaera.response;

import java.util.List;

public class GetsResponse extends Response {

    private List<byte[]> values;

    public List<byte[]> getValues() {
        return values;
    }

    public void setValues(List<byte[]> values) {
        this.values = values;
    }
}
