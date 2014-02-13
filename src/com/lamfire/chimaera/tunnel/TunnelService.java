package com.lamfire.chimaera.tunnel;

import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.config.TunnelConfigure;
import com.lamfire.code.PUID;
import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import com.lamfire.utils.StringUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-22
 * Time: 上午10:13
 * To change this template use File | Settings | File Templates.
 */
public class TunnelService {
    private static final Logger LOGGER = Logger.getLogger(TunnelService.class);
    private String clientId = PUID.puidAsString();
    private ChimaeraCli cli;
    private TunnelSetting setting;
    private boolean tunnelStartup = false;

    public TunnelService(TunnelSetting setting) {
        this.setting = setting;
    }

    public synchronized void startup(){
        if(!tunnelStartup){
            openConnection();
            openTunnels();
        }
        this.tunnelStartup = true;
    }

    private synchronized void openConnection(){
        if(cli != null){
             return ;
        }
        cli = new ChimaeraCli();
        cli.setMaxConnections(1);
        cli.open(setting.getHost(), setting.getPort());
        LOGGER.info("connecting to:" +setting.getHost() +":" + setting.getPort());
    }

    private synchronized void openTunnels(){
        List<TunnelConfigure> list = setting.getTunnelList();
        for(TunnelConfigure conf : list){
            LOGGER.info("Setting Tunnel:" + JSON.toJSONString(conf));
            if(StringUtils.equals(TunnelConfigure.TYPE_SUBSCRIBE ,conf.getFromType())){
                if(StringUtils.equals(TunnelConfigure.TYPE_SUBSCRIBE ,conf.getToType())){
                    cli.getSubscribe().bind(conf.getFromKey(),clientId,new ToSubscribeTunnel(conf.getToKey()));
                    LOGGER.info("Tunnel subscribe["+conf.getFromKey()+"] to subscribe["+conf.getToKey()+"]");
                }else if(StringUtils.equals(TunnelConfigure.TYPE_POLLER ,conf.getToType())){
                    cli.getSubscribe().bind(conf.getFromKey(),clientId,new ToPollerTunnel(conf.getToKey()));
                    LOGGER.info("Tunnel subscribe["+conf.getFromKey()+"] to poller["+conf.getToKey()+"]");
                }else{
                    LOGGER.warn("Failed to type["+conf.getToType()+"]:" + JSON.toJSONString(conf));
                }
            }else if(StringUtils.equals(TunnelConfigure.TYPE_POLLER ,conf.getFromType())){
                if(StringUtils.equals(TunnelConfigure.TYPE_SUBSCRIBE ,conf.getToType())){
                    cli.getPoller().bind(conf.getFromKey(),clientId,new ToSubscribeTunnel(conf.getToKey()));
                    LOGGER.info("Tunnel poller["+conf.getFromKey()+"] to subscribe["+conf.getToKey()+"]");
                }else if(StringUtils.equals(TunnelConfigure.TYPE_POLLER ,conf.getToType())){
                    cli.getPoller().bind(conf.getFromKey(),clientId,new ToPollerTunnel(conf.getToKey()));
                    LOGGER.info("Tunnel poller["+conf.getFromKey()+"] to poller["+conf.getToKey()+"]");
                }else{
                    LOGGER.warn("Failed to type["+conf.getToType()+"]:" + JSON.toJSONString(conf));
                }
            }else{
                LOGGER.warn("Failed to type["+conf.getToType()+"]:" + JSON.toJSONString(conf));
            }
        }
    }

}
