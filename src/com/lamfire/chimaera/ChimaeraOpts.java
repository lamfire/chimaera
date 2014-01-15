package com.lamfire.chimaera;

import com.lamfire.logger.Logger;
import com.lamfire.utils.FileUtils;
import com.lamfire.utils.NumberUtils;
import com.lamfire.utils.PropertiesUtils;
import com.lamfire.utils.StringUtils;

import java.io.File;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-3
 * Time: 下午6:07
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraOpts {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraOpts.class);
    public static final String CONFIG_RESOURCE_NAME = "chimaera.properties";
    private String bind = "0.0.0.0";
    private int port = 19800;
    private boolean storeInMemory = true;
    private String storeDir;
    private int storeCacheSize = -1;
    private int threads = 16;

    static ChimaeraOpts instance;

    public synchronized static ChimaeraOpts get(){
        if(instance == null){
            instance = new ChimaeraOpts();
        }
        return instance;
    }

    private  ChimaeraOpts(){
        try{
            Properties prop = PropertiesUtils.load(CONFIG_RESOURCE_NAME,ChimaeraOpts.class);
            this.bind = (String)prop.get("bind");
            this.port = Integer.parseInt((String)prop.get("port"));
            this.threads = Integer.parseInt((String)prop.get("threads"));
            String store = (String)prop.get("store");
            if(StringUtils.equalsIgnoreCase("file",store)){
                this.storeInMemory = false;
            }else{
                this.storeInMemory = true;
            }
            this.storeDir = (String)prop.get("store.dir");
            this.storeCacheSize = Integer.parseInt((String)prop.get("store.cache.size"));
        }catch (Exception e){
            LOGGER.warn("Parse '"+CONFIG_RESOURCE_NAME+"' file failed,use memory store.");
        }

        initOptions();
    }

    private void initOptions(){
        LOGGER.info("bind:" + bind);
        LOGGER.info("port:" +port);
        LOGGER.info("store:" +(storeInMemory?"memory":"file"));
        if(! storeInMemory){
            LOGGER.info("storeDir:" +storeDir);
            if(!FileUtils.exists(storeDir)){
                FileUtils.makeDirs(storeDir);
                LOGGER.info("The store dir not found,make " + this.storeDir);
            }
            LOGGER.info("storeCacheSize:" +storeCacheSize);
        }
    }

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

    public boolean isStoreInMemory() {
        return storeInMemory;
    }

    public void setStoreInMemory(boolean storeInMemory) {
        this.storeInMemory = storeInMemory;
    }

    public String getStoreDir() {
        return storeDir;
    }

    public void setStoreDir(String storeDir) {
        this.storeDir = storeDir;
    }

    public int getStoreCacheSize() {
        return storeCacheSize;
    }

    public void setStoreCacheSize(int storeCacheSize) {
        this.storeCacheSize = storeCacheSize;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}
