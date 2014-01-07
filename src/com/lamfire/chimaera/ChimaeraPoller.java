package com.lamfire.chimaera;

import com.lamfire.chimaera.response.subscribe.PublishResponse;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.hydra.CycleSessionIterator;
import com.lamfire.hydra.Message;
import com.lamfire.hydra.net.Session;
import com.lamfire.hydra.net.SessionGroup;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Maps;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-7
 * Time: 下午7:20
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraPoller{
    private static final Logger LOGGER = Logger.getLogger(ChimaeraPoller.class);
    private  final Map<String,SessionGroup> groups = Maps.newHashMap();
    private static final ChimaeraPoller instance = new ChimaeraPoller();

    public static final ChimaeraPoller getInstance(){
        return   instance;
    }

    private ChimaeraPoller(){

    }

    public  synchronized SessionGroup getSesionGroup(String key){
        SessionGroup group = groups.get(key);
        if(group == null){
            group = new SessionGroup();
            groups.put(key,group);
        }
        return group;
    }

    public synchronized  void bind(String key,String clientId,Session session){
        SessionGroup group = getSesionGroup(key);
        if(group.existsKey(clientId)){
            Session old = group.remove(clientId);
            LOGGER.info("["+clientId + "] change["+old+"] to["+session+"]");
        }
        group.add(clientId, session);
    }

    public  void unbind(String key,String clientId){
        getSesionGroup(key).remove(clientId);
    }

    public  void publish(String key,byte[] bytes){
        SessionGroup group = getSesionGroup(key);
        PublishResponse response = new PublishResponse();
        response.setKey(key);
        response.setMessage(bytes);

        byte[] responseBytes =  Serializers.getResponseSerializer().encode(response);
        Message msg = new Message();
        msg.setBody(responseBytes);

        //获得下个Session
        Session session = group.next();
        if(session != null){
            sendResponse(session,msg);
        }else{
            LOGGER.warn("Not found available poller session.");
        }
    }

    private void sendResponse(Session session,Message message){
        try{
            session.send(message);
        }catch(Exception e){
            LOGGER.error("error send response.",e);
        }
    }
}
