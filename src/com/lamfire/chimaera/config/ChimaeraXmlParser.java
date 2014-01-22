package com.lamfire.chimaera.config;

import com.lamfire.chimaera.drainage.DrainageSetting;
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
            getDrainageConfigureList();
        }catch (Exception e){
            LOGGER.error("[ERROR]:" + e.getMessage(),e);
        }
    }

    private ServerConfigure serverConfigure;
    private List<DrainageSetting> drainages;

    public synchronized ServerConfigure getServerConfigure() throws XPathExpressionException {
        if(serverConfigure != null){
            return serverConfigure;
        }

        serverConfigure = new ServerConfigure();
        XMLParser parser = XMLParser.load(XML_RESOURCE,ChimaeraXmlParser.class);
        String bind = parser.getNodeAttribute("/chimaera/server","bind");
        int port = Integer.parseInt(parser.getNodeAttribute("/chimaera/server","port"));
        int threads = Integer.parseInt(parser.getNodeValue("/chimaera/server/threads"));

        String type = parser.getNodeAttribute("/chimaera/server/store", "type");

        serverConfigure.setBind(bind);
        serverConfigure.setPort(port);
        serverConfigure.setThreads(threads);
        serverConfigure.setStoreInMemory(true);
        if(!StringUtils.equalsIgnoreCase("memory",type)){
            boolean enableSoftCache =  Boolean.parseBoolean(parser.getNodeValue("/chimaera/server/store/enableSoftCache"));
            boolean enableLocking =  Boolean.parseBoolean(parser.getNodeValue("/chimaera/server/store/enableLocking"));
            boolean enableTransactions =  Boolean.parseBoolean(parser.getNodeValue("/chimaera/server/store/enableTransactions"));
            int flushThresholdOps = Integer.parseInt(parser.getNodeValue("/chimaera/server/store/flushThresholdOps"));
            int flushInterval = Integer.parseInt(parser.getNodeValue("/chimaera/server/store/flushInterval"));
            String storeDir = parser.getNodeValue("/chimaera/server/store/fileDir");
            serverConfigure.setStoreInMemory(false);
            serverConfigure.setEnableLocking(enableLocking);
            serverConfigure.setEnableSoftCache(enableSoftCache);
            serverConfigure.setEnableTransactions(enableTransactions);
            serverConfigure.setFlushThresholdOps(flushThresholdOps);
            serverConfigure.setFlushInterval(flushInterval);
            serverConfigure.setStoreDir(storeDir);
        }
        LOGGER.info("[SERVER]:" + JSON.toJSONString(serverConfigure));
        return serverConfigure;
    }


    public synchronized List<DrainageSetting> getDrainageConfigureList() throws XPathExpressionException {
        if(drainages != null){
            return drainages;
        }

        drainages = new ArrayList<DrainageSetting>();
        XMLParser parser = XMLParser.load(XML_RESOURCE,ChimaeraXmlParser.class);
        NodeList list = parser.getNodeList("/chimaera/drainage");
        if(list.getLength() == 0){
            return  drainages;
        }

        for(int i=0;i< list.getLength();i++){
            Node node = list.item(i);
            String host = node.getAttributes().getNamedItem("host").getNodeValue();
            int port = Integer.parseInt(node.getAttributes().getNamedItem("port").getNodeValue());
            DrainageSetting setting = new DrainageSetting();
            Node child = node.getFirstChild();
            while(child != null){
                DrainageConfigure conf = parseDrainageConfigure(parser,child,host,port);
                if(conf != null){
                    setting.addDrainageConfigure(conf);
                    LOGGER.info("[FOUND DRAINAGE]:" + JSON.toJSONString(conf));
                }
                child = child.getNextSibling();
            }
            drainages.add(setting);
        }

        return drainages;
    }

    private DrainageConfigure parseDrainageConfigure(XMLParser parser,Node node,String host,int port){
        DrainageConfigure conf = new DrainageConfigure();

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
