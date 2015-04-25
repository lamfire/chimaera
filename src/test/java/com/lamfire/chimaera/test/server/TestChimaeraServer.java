package com.lamfire.chimaera.test.server;

import com.lamfire.chimaera.ChimaeraOpts;
import com.lamfire.chimaera.ChimaeraServer;
import com.lamfire.chimaera.bootstrap.ChimaeraBootstrap;
import com.lamfire.logger.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-30
 * Time: 下午5:16
 * To change this template use File | Settings | File Templates.
 */
public class TestChimaeraServer {
    private static final Logger LOGGER = Logger.getLogger(TestChimaeraServer.class);
    public static void main(String[] args) throws Exception {
        ChimaeraBootstrap.main(args);
    }
}
