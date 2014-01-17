package com.lamfire.chimaera.response;

public class ExistsResponse extends Response {
    private boolean exists;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
