package com.lamfire.chimaera.http;

import com.lamfire.chimaera.config.ChimaeraXmlParser;
import com.lamfire.chimaera.config.ServerConfigure;
import com.lamfire.logger.Logger;
import com.lamfire.warden.ActionRegistry;
import com.lamfire.warden.HttpServer;

import javax.xml.xpath.XPathExpressionException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-5-6
 * Time: 上午10:44
 * To change this template use File | Settings | File Templates.
 */
public class HttpServerBootstrap{
    private static final Logger LOGGER = Logger.getLogger(HttpServerBootstrap.class);


    public void startup() throws XPathExpressionException {
        ActionRegistry registry = new ActionRegistry();
        registry.mapping(ActionService.class);

        ServerConfigure conf = ChimaeraXmlParser.get().getServerConfigure();
        HttpServer server = new HttpServer(conf.getBind(),conf.getHttpPort());
        server.startup(registry);
    }
}
