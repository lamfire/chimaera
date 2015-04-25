package com.lamfire.chimaera.response.map;

import com.lamfire.chimaera.response.Response;

import java.util.List;

public class MapKeysResponse extends Response {

    private List<String> keys;

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
