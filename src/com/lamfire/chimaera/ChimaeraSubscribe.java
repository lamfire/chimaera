package com.lamfire.chimaera;

import com.lamfire.hydra.Message;
import com.lamfire.hydra.net.Session;
import com.lamfire.hydra.net.SessionGroup;
import com.lamfire.chimaera.response.subscribe.PublishResponse;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Maps;
import com.lamfire.utils.StringUtils;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-11-14
 * Time: 上午10:50
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraSubscribe {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraSubscribe.class);
    private  final Map<String,SessionGroup> groups = Maps.newHashMap();
    public static final String SessionAttrKey = "_SUBSCRIBE_ATTR_KEY";

    private static final ChimaeraSubscribe instance = new ChimaeraSubscribe();

    public static final ChimaeraSubscribe getInstance(){
        return   instance;
    }

    private ChimaeraSubscribe(){}

    public  synchronized SessionGroup getSesionGroup(String key){
        SessionGroup group = groups.get(key);
        if(group == null){
            group = new SessionGroup();
            groups.put(key,group);
        }
        return group;
    }
    public  void bind(String key,String clientId,Session session){
        session.setAttribute(SessionAttrKey,clientId);
        getSesionGroup(key).add(clientId,session);
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
        for(Session s :group.sessions()){
            sendResponse(s, msg);
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
