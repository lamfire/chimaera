package com.lamfire.chimaera.drainage;

import com.lamfire.chimaera.client.ChimaeraCli;
import com.lamfire.chimaera.config.DrainageConfigure;
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
public class DrainageService {
    private static final Logger LOGGER = Logger.getLogger(DrainageService.class);
    private String clientId = PUID.puidAsString();
    private ChimaeraCli cli;
    private DrainageSetting setting;
    private boolean drainageStartup = false;

    public DrainageService(DrainageSetting setting) {
        this.setting = setting;
    }

    public synchronized void startup(){
        if(!drainageStartup){
            openConnection();
            openDrainage();
        }
        this.drainageStartup = true;
    }

    private synchronized void openConnection(){
        if(cli != null){
             return ;
        }
        cli = new ChimaeraCli();
        cli.open(setting.getHost(), setting.getPort());
        LOGGER.info("connecting to:" +setting.getHost() +":" + setting.getPort());
    }

    private synchronized void openDrainage(){
        List<DrainageConfigure> list = setting.getDrainageList();
        for(DrainageConfigure conf : list){
            LOGGER.info("Setting drainage:" + JSON.toJSONString(conf));
            if(StringUtils.equals(DrainageConfigure.TYPE_SUBSCRIBE ,conf.getFromType())){
                if(StringUtils.equals(DrainageConfigure.TYPE_SUBSCRIBE ,conf.getToType())){
                    cli.getSubscribe().bind(conf.getFromKey(),clientId,new ToSubscribeDrainage());
                    LOGGER.info("drainage subscribe["+conf.getFromKey()+"] to subscribe["+conf.getToKey()+"]");
                }else if(StringUtils.equals(DrainageConfigure.TYPE_POLLER ,conf.getToType())){
                    cli.getSubscribe().bind(conf.getFromKey(),clientId,new ToPollerDrainage());
                    LOGGER.info("drainage subscribe["+conf.getFromKey()+"] to poller["+conf.getToKey()+"]");
                }else{
                    LOGGER.warn("Failed to type["+conf.getToType()+"]:" + JSON.toJSONString(conf));
                }
            }else if(StringUtils.equals(DrainageConfigure.TYPE_POLLER ,conf.getFromType())){
                if(StringUtils.equals(DrainageConfigure.TYPE_SUBSCRIBE ,conf.getToType())){
                    cli.getPoller().bind(conf.getFromKey(),clientId,new ToSubscribeDrainage());
                    LOGGER.info("drainage poller["+conf.getFromKey()+"] to subscribe["+conf.getToKey()+"]");
                }else if(StringUtils.equals(DrainageConfigure.TYPE_POLLER ,conf.getToType())){
                    cli.getPoller().bind(conf.getFromKey(),clientId,new ToPollerDrainage());
                    LOGGER.info("drainage poller["+conf.getFromKey()+"] to poller["+conf.getToKey()+"]");
                }else{
                    LOGGER.warn("Failed to type["+conf.getToType()+"]:" + JSON.toJSONString(conf));
                }
            }else{
                LOGGER.warn("Failed to type["+conf.getToType()+"]:" + JSON.toJSONString(conf));
            }
        }
    }

}
