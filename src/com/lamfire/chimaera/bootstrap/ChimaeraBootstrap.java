package com.lamfire.chimaera.bootstrap;

import com.lamfire.chimaera.ChimaeraOpts;
import com.lamfire.chimaera.ChimaeraServer;
import com.lamfire.chimaera.config.ChimaeraXmlParser;
import com.lamfire.chimaera.config.ServerConfigure;
import com.lamfire.chimaera.http.HttpServerBootstrap;
import com.lamfire.logger.Logger;
import com.lamfire.utils.FileUtils;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-3
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraBootstrap {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraBootstrap.class);
    private ServerConfigure serverConfigure;

    ChimaeraBootstrap(){
        initOpts();
    }

    private void renewStoreDir(String dir) throws IOException {
        File sourceDir = new File(dir);
        FileUtils.cleanDirectory(sourceDir);
    }

    private void initOpts() {
        try {
            serverConfigure = ChimaeraXmlParser.get().getServerConfigure();
            if((serverConfigure.isStoreOnDisk()) && serverConfigure.isRenew()){
                String storeDir = serverConfigure.getStoreDir();
                LOGGER.info("Configure store in file,setting 'renew' is true,clean store directory : " +storeDir );
                renewStoreDir(storeDir);
            }
        } catch (Exception e) {
            LOGGER.warn("Parse '" + ChimaeraXmlParser.XML_RESOURCE + "' file failed,use memory store.");
        }
        printOptions();
    }

    private void printOptions() {
        LOGGER.info("bind:" + serverConfigure.getBind());
        LOGGER.info("port:" + serverConfigure.getPort());
        LOGGER.info("store:" + (serverConfigure.isStoreOnDisk() ? "disk":"memory"));
        if (serverConfigure.isStoreOnDisk()) {
            LOGGER.info("StoreDir:" + serverConfigure.getStoreDir());
            if (!FileUtils.exists(serverConfigure.getStoreDir())) {
                FileUtils.makeDirs(serverConfigure.getStoreDir());
                LOGGER.info("The store dir not found,make " + serverConfigure.getStoreDir());
            }
            LOGGER.info("FlushThresholdOps:" + serverConfigure.getFlushThresholdOps());
            LOGGER.info("FlushInterval:" + serverConfigure.getFlushInterval());
            LOGGER.info("EnableLocking:" + serverConfigure.isEnableLocking());
            LOGGER.info("EnableCache:" + serverConfigure.isEnableCache());
            LOGGER.info("CacheSize:" + serverConfigure.getCacheSize());
            LOGGER.info("EnableTransactions:" + serverConfigure.isEnableTransactions());
        }
    }


    public void startup(){
        ChimaeraServer server = new ChimaeraServer(serverConfigure);
        server.startup();
        server.startupTunnels();
    }

    public static void main(String[] args) throws Exception {
        ChimaeraBootstrap bootstrap = new ChimaeraBootstrap();
        bootstrap.startup();

        HttpServerBootstrap httpBootstrap = new HttpServerBootstrap();
        httpBootstrap.startup();

    }
}
