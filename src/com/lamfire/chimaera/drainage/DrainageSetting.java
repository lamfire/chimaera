package com.lamfire.chimaera.drainage;

import com.lamfire.chimaera.config.DrainageConfigure;
import com.lamfire.utils.Lists;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-22
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class DrainageSetting {
    private String host;
    private int port;
    private final List<DrainageConfigure> drainageList = Lists.newArrayList();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void addDrainageConfigure(DrainageConfigure configure){
        this.drainageList.add(configure);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<DrainageConfigure> getDrainageList() {
        return drainageList;
    }
}
