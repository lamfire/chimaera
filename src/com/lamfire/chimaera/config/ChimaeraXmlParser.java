package com.lamfire.chimaera.config;

import com.lamfire.chimaera.tunnel.TunnelSetting;
import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import com.lamfire.utils.StringUtils;
import com.lamfire.utils.XMLParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-21
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraXmlParser {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraXmlParser.class);
    public static final String XML_RESOURCE = "chimaera.xml";

    private static ChimaeraXmlParser instance = new ChimaeraXmlParser();

    public static ChimaeraXmlParser get(){
        return instance;
    }

    private ChimaeraXmlParser(){
        try{
            getServerConfigure();
            getTunnelConfigureList();
        }catch (Exception e){
            LOGGER.error("[ERROR]:" + e.getMessage(),e);
        }
    }

    private ServerConfigure serverConfigure;
    private List<TunnelSetting> tunnels;

    public synchronized ServerConfigure getServerConfigure() throws XPathExpressionException {
        if(serverConfigure != null){
            return serverConfigure;
        }

        serverConfigure = new ServerConfigure();
        XMLParser parser = XMLParser.load(XML_RESOURCE,ChimaeraXmlParser.class);
        String bind = parser.getNodeAttribute("/chimaera/server","bind");
        int port = Integer.parseInt(parser.getNodeAttribute("/chimaera/server","port"));
        int httpPort = Integer.parseInt(parser.getNodeAttribute("/chimaera/server","httpPort"));
        int threads = Integer.parseInt(parser.getNodeValue("/chimaera/server/threads"));

        String type = parser.getNodeAttribute("/chimaera/server/store", "type");

        serverConfigure.setBind(bind);
        serverConfigure.setPort(port);
        serverConfigure.setHttpPort(httpPort);
        serverConfigure.setThreads(threads);
        serverConfigure.setStoreOnDisk(false);
        if(!StringUtils.equalsIgnoreCase("memory",type)){
            boolean renew =  Boolean.parseBoolean(parser.getNodeValue("/chimaera/server/store/renew"));
            boolean enableSoftCache =  Boolean.parseBoolean(parser.getNodeValue("/chimaera/server/store/enableCache"));
            boolean enableLocking =  Boolean.parseBoolean(parser.getNodeValue("/chimaera/server/store/enableLocking"));
            boolean enableTransactions =  Boolean.parseBoolean(parser.getNodeValue("/chimaera/server/store/enableTransactions"));
            int cacheSize = Integer.parseInt(parser.getNodeValue("/chimaera/server/store/cacheSize"));
            int flushThresholdOps = Integer.parseInt(parser.getNodeValue("/chimaera/server/store/flushThresholdOps"));
            int flushInterval = Integer.parseInt(parser.getNodeValue("/chimaera/server/store/flushInterval"));
            String storeDir = parser.getNodeValue("/chimaera/server/store/fileDir");
            serverConfigure.setStoreOnDisk(true);
            serverConfigure.setRenew(renew);
            serverConfigure.setEnableLocking(enableLocking);
            serverConfigure.setEnableCache(enableSoftCache);
            serverConfigure.setEnableTransactions(enableTransactions);
            serverConfigure.setFlushThresholdOps(flushThresholdOps);
            serverConfigure.setFlushInterval(flushInterval);
            serverConfigure.setStoreDir(storeDir);
            serverConfigure.setCacheSize(cacheSize);
        }
        serverConfigure.setTunnelSettings(getTunnelConfigureList());
        LOGGER.info("[SERVER]:" + JSON.toJSONString(serverConfigure));
        return serverConfigure;
    }


    public synchronized List<TunnelSetting> getTunnelConfigureList() throws XPathExpressionException {
        if(tunnels != null){
            return tunnels;
        }

        tunnels = new ArrayList<TunnelSetting>();
        XMLParser parser = XMLParser.load(XML_RESOURCE,ChimaeraXmlParser.class);
        NodeList list = parser.getNodeList("/chimaera/tunnel");
        if(list.getLength() == 0){
            return tunnels;
        }

        for(int i=0;i< list.getLength();i++){
            Node node = list.item(i);
            String host = node.getAttributes().getNamedItem("host").getNodeValue();
            int port = Integer.parseInt(node.getAttributes().getNamedItem("port").getNodeValue());
            TunnelSetting setting = new TunnelSetting();
            setting.setHost(host);
            setting.setPort(port);

            Node child = node.getFirstChild();
            while(child != null){
                TunnelConfigure conf = parseTunnelConfigure(parser,child,host,port);
                if(conf != null){
                    setting.addTunnelConfigure(conf);
                    LOGGER.info("[FOUND DRAINAGE]:" + JSON.toJSONString(conf));
                }
                child = child.getNextSibling();
            }
            tunnels.add(setting);
        }

        return tunnels;
    }

    private TunnelConfigure parseTunnelConfigure(XMLParser parser,Node node,String host,int port){
        TunnelConfigure conf = new TunnelConfigure();

        String fromType = null;
        if(StringUtils.equals(node.getNodeName(),"subscribe")){
            fromType = "subscribe";
        }else if(StringUtils.equals(node.getNodeName(),"poller")){
            fromType = "poller";
        }else{
            return null;
        }

        String fromKey = node.getAttributes().getNamedItem("key").getNodeValue();
        Node destNode = parser.getChildNode(node,"destination");
        if(destNode == null){
            LOGGER.warn("parse '"+XML_RESOURCE +"' error,not found node [destination],igrone " + node.toString());
            return null;
        }
        String toKey = destNode.getAttributes().getNamedItem("key").getNodeValue();
        String toType = null;
        if(StringUtils.equals(destNode.getAttributes().getNamedItem("type").getNodeValue(),"subscribe")){
            toType = "subscribe";
        }else if(StringUtils.equals(destNode.getAttributes().getNamedItem("type").getNodeValue(),"poller")){
            toType = "poller";
        }

        conf.setHost(host);
        conf.setPort(port);
        conf.setFromKey(fromKey);
        conf.setFromType(fromType);
        conf.setToKey(toKey);
        conf.setToType(toType);
        return conf;
    }
}
