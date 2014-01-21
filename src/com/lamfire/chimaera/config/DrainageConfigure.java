package com.lamfire.chimaera.config;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-21
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public class DrainageConfigure {
    public static final String TYPE_SUBSCRIBE = "subscribe";
    public static final String TYPE_POLLER = "poller";

    private String host;
    private int port;
    private String fromType;
    private String toType;
    private String fromKey;
    private String toKey;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getFromKey() {
        return fromKey;
    }

    public void setFromKey(String fromKey) {
        this.fromKey = fromKey;
    }

    public String getToKey() {
        return toKey;
    }

    public void setToKey(String toKey) {
        this.toKey = toKey;
    }
}
