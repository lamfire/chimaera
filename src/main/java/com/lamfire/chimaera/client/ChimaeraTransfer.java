package com.lamfire.chimaera.client;

import com.lamfire.chimaera.ChimaeraException;
import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.subscribe.PublishResponse;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.hydra.*;
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
    private final Map<String, OnMessageListener> subscribeMessageListeners = Maps.newHashMap();
    private final Map<String, OnMessageListener> pollerMessageListeners = Maps.newHashMap();
    private static final AtomicInteger MessageID = new AtomicInteger();
    private ResponseWaitQueue waitQueue;

    public ChimaeraTransfer(String host, int port, ResponseWaitQueue waitQueue) {
        super(host, port);
        super.setAutoConnectRetry(true);
        super.setKeepAlive(true);
        this.waitQueue = waitQueue;
    }

    public ChimaeraTransfer(String host, int port, int threads, ResponseWaitQueue waitQueue) {
        this(host, port, waitQueue);
        super.setKeepaliveConnsWithClient(threads);
    }

    public void setSubscribeMessageListener(String key, OnMessageListener listener) {
        this.subscribeMessageListeners.put(key, listener);
    }

    public void removeSubscribeMessageListener(String key) {
        this.subscribeMessageListeners.remove(key);
    }

    public OnMessageListener getSubscribeMessageListener(String key) {
        return this.subscribeMessageListeners.get(key);
    }

    public void setPollerMessageListener(String key, OnMessageListener listener) {
        this.pollerMessageListeners.put(key, listener);
    }

    public void removePollerMessageListener(String key) {
        this.pollerMessageListeners.remove(key);
    }

    public OnMessageListener getPollerMessageListener(String key) {
        return this.pollerMessageListeners.get(key);
    }

    private int getMessageId() {
        return MessageID.getAndIncrement();
    }

    private Session getSession() {
        Collection<Session> sessions = super.getSessions();
        if (sessions == null) {
            throw new ChimaeraException("not connection found.");
        }
        Session s = awaitAvailableSession();
        if (s == null) {
            throw new ChimaeraException("not available sessions");
        }
        return s;
    }

    private void sendBytes(Session session, int messageId, byte[] bytes) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SEND_COMMAND:" + new String(bytes));
        }
        try{
            session.send(new Message(messageId, bytes)).sync();
        }catch (Throwable e){
            LOGGER.error(e.getMessage(),e);
        }
    }

    public ResponseFuture sendCommand(Command command, Class<?> responseType) {
        byte[] bytes = Serializers.getCommandSerializer().encode(command);
        int messageId = getMessageId();
        Session session = getSession();
        ResponseFuture future = new ResponseFuture(session, command, messageId, waitQueue, responseType);
        sendBytes(session, messageId, bytes);
        return future;
    }

    public void sendCommand(Command command) {
        byte[] bytes = Serializers.getCommandSerializer().encode(command);
        sendBytes(getSession(), getMessageId(), bytes);
    }

    @Override
    protected void handleMessage(MessageContext context, Message message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleMessage : " + message);
        }
        String js = new String(message.getBody());
        JSON json = JSON.fromJSONString(js);
        Integer status = (Integer) json.get("status");

        //subscribe response
        if (status == PublishResponse.STATUS) {
            PublishResponse response = (PublishResponse) Serializers.getResponseSerializer().decode(json, Response.class);
            OnMessageListener listener = null;
            if(response.getType() == PublishResponse.TYPE_SUBSCRIBE){
                listener = getSubscribeMessageListener(response.getKey());
            }else if(response.getType() == PublishResponse.TYPE_POLLER){
                listener = getPollerMessageListener(response.getKey());
            } else{
                LOGGER.warn("Not support publish response type:" + response.getType());
            }
            if (listener == null) {
                LOGGER.warn("Not found message listener :" +response.getKey()+"/" + response.getType());
                return;
            }
            listener.onMessage(response.getKey(), response.getMessage());
            return;
        }

        //future response
        ResponseFuture future = waitQueue.remove(message.getId());
        if (future == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("received message[" + message.getId() + "],but not found the wait future.");
            }
            return;
        }
        Response response = Serializers.getResponseSerializer().decode(json, future.getResponseType());
        if (response != null) {
            future.onResponse(response);
        }
    }

}
