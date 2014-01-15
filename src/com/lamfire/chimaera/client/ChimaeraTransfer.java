package com.lamfire.chimaera.client;

import com.lamfire.chimaera.ChimaeraException;
import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.response.Response;
import com.lamfire.hydra.*;
import com.lamfire.chimaera.command.Command;
import com.lamfire.hydra.net.Session;
import com.lamfire.chimaera.response.subscribe.PublishResponse;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-10-11
 * Time: 下午5:36
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraTransfer extends Snake {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraTransfer.class);
    private final Map<String,OnMessageListener> onMessageListeners = Maps.newHashMap();
    private static final AtomicInteger MessageID = new AtomicInteger();
    private ResponseWaitQueue waitQueue;
    private CycleSessionIterator sessionIt;

    public ChimaeraTransfer(String host, int port, ResponseWaitQueue waitQueue) {
        super(host, port);
        super.setAutoConnectRetry(true);
        super.setKeepAlive(true);
        this.waitQueue = waitQueue;
        this.sessionIt = new CycleSessionIterator(this);
    }

    public ChimaeraTransfer(String host, int port, int threads, ResponseWaitQueue waitQueue) {
        this(host, port,waitQueue);
        super.setKeepaliveConnsWithClient(threads);
    }

    public void bindMessageListener(String key, OnMessageListener listener){
        this.onMessageListeners.put(key, listener);
    }

    public void unbindMessageListener(String key){
        this.onMessageListeners.remove(key);
    }

    public OnMessageListener getMessageListener(String key){
        return this.onMessageListeners.get(key);
    }

    private int getMessageId(){
         return MessageID.getAndIncrement();
    }

    private void sendBytes(int messageId,byte[] bytes){
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("SEND_COMMAND:"+new String(bytes));
        }
        Collection<Session> sessions = super.getSessions();
        if(sessions == null){
            throw new ChimaeraException("not connection found.");
        }
        Session s = sessionIt.nextAvailableSession();
        if(s == null){
            throw new ChimaeraException("not available sessions");
        }
        s.send(new Message(messageId,bytes));
    }

    public ResponseFuture sendCommand(Command command,Class<?> responseType){
        byte[] bytes = Serializers.getCommandSerializer().encode(command);
        int messageId = getMessageId();
        ResponseFuture future = new ResponseFuture(command,messageId,waitQueue,responseType);
        sendBytes(messageId,bytes);
        return future;
    }

    public void sendCommand(Command command){
        byte[] bytes = Serializers.getCommandSerializer().encode(command);
        sendBytes(getMessageId(),bytes);
    }

    @Override
    protected void handleMessage(MessageContext context, Message message) {
        if(LOGGER.isDebugEnabled() ){
            LOGGER.debug("handleMessage : "+message);
        }
        String js = new String(message.getBody());
        JSON json = new JSON(js);
        Integer status = (Integer)json.get("status");

        //subscribe response
        if(status == PublishResponse.STATUS){
            PublishResponse response =  (PublishResponse)Serializers.getResponseSerializer().decode(json,Response.class);
            OnMessageListener listener = this.getMessageListener(response.getKey());
            if(listener != null){
                listener.onMessage(response.getKey(),response.getMessage());
            }
            return ;
        }

        //future response
        ResponseFuture future = waitQueue.remove(message.getId());
        if(future == null){
            if(LOGGER.isDebugEnabled() ){
                LOGGER.debug("received message["+message.getId()+"],but not found the wait future.");
            }
            return ;
        }
        Response response =Serializers.getResponseSerializer().decode(json,future.getResponseType());
        if(response != null){
            future.onResponse(response);
        }
    }

}
