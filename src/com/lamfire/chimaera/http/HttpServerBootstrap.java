package com.lamfire.chimaera.http;

import com.lamfire.chimaera.ServiceRegistry;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.config.ChimaeraXmlParser;
import com.lamfire.chimaera.config.ServerConfigure;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

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
        registry.mapping("/api",new ActionService());

        ServerConfigure conf = ChimaeraXmlParser.get().getServerConfigure();
        HttpServer server = new HttpServer(registry,conf.getBind(),conf.getHttpPort());
        server.startup();
    }
}
