package com.lamfire.chimaera.bootstrap;

import com.lamfire.chimaera.ChimaeraOpts;
import com.lamfire.chimaera.ChimaeraServer;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-3
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraBootstrap {
    public static void main(String[] args) {
        ChimaeraOpts opts = ChimaeraOpts.get();
        ChimaeraServer.startup(opts.getBind(), opts.getPort());
    }
}
