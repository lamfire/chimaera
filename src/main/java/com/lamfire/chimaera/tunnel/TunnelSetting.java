package com.lamfire.chimaera.tunnel;

import com.lamfire.chimaera.config.TunnelConfigure;
import com.lamfire.utils.Lists;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-22
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class TunnelSetting {
    private String host;
    private int port;
    private final List<TunnelConfigure> tunnelList = Lists.newArrayList();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void addTunnelConfigure(TunnelConfigure configure){
        this.tunnelList.add(configure);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<TunnelConfigure> getTunnelList() {
        return tunnelList;
    }
}
