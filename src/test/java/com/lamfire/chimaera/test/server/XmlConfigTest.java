package com.lamfire.chimaera.test.server;

import com.lamfire.chimaera.config.ChimaeraXmlParser;
import com.lamfire.chimaera.config.ServerConfigure;
import com.lamfire.json.JSON;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-21
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
public class XmlConfigTest {

    public static void main(String[] args) throws Exception {
        ChimaeraXmlParser parser = ChimaeraXmlParser.get();
        ServerConfigure config = parser.getServerConfigure();

        System.out.println(JSON.toJSONString(config));
        System.out.println(JSON.toJSONString(parser.getTunnelConfigureList()));
    }
}
