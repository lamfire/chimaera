package com.lamfire.chimaera.response;

import java.util.Set;

public class KeysResponse extends Response {
    private Set<String> keys;

    public Set<String> getKeys() {
        return keys;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }
}
