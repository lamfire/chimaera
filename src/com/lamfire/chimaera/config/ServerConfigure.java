package com.lamfire.chimaera.config;

import com.lamfire.chimaera.ChimaeraOpts;
import com.lamfire.chimaera.tunnel.TunnelSetting;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-21
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class ServerConfigure extends ChimaeraOpts {
    private String bind = "0.0.0.0";
    private int port = 19800;
    private int httpPort = 19900;
    private int threads = 16;
    private boolean renew = false;
    private List<TunnelSetting> TunnelSettings;

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public boolean isRenew() {
        return renew;
    }

    public void setRenew(boolean renew) {
        this.renew = renew;
    }

    public List<TunnelSetting> getTunnelSettings() {
        return TunnelSettings;
    }

    public void setTunnelSettings(List<TunnelSetting> tunnelSettings) {
        TunnelSettings = tunnelSettings;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }
}
