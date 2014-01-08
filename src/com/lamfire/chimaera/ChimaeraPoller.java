package com.lamfire.chimaera;

import com.lamfire.chimaera.response.subscribe.PublishResponse;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.chimaera.store.FireQueue;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.CycleSessionIterator;
import com.lamfire.hydra.Message;
import com.lamfire.hydra.net.Session;
import com.lamfire.hydra.net.SessionGroup;
import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Maps;
import com.lamfire.utils.Threads;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-7
 * Time: 下午7:20
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraPoller implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(ChimaeraPoller.class);
    private  final Map<String,SessionGroup> groups = Maps.newHashMap();
    private static final ChimaeraPoller instance = new ChimaeraPoller();
    public static final String STORE_NAME = "CHIMAERA_POLLER";
    public static final String QUEUE_NAME = "CHIMAERA_POLLER_QUEUE";

    public static final ChimaeraPoller getInstance(){
        return   instance;
    }

    private FireStore store;   //store
    private ChimaeraBlockingQueue queue;   //消息队列
    private ExecutorService service; //消息分发服务

    private ChimaeraPoller(){
        this.store = Chimaera.getFireStore(STORE_NAME);
        this.queue = new ChimaeraBlockingQueue(this.store.getFireQueue(QUEUE_NAME));
        this.service = Executors.newFixedThreadPool(1, Threads.makeThreadFactory(STORE_NAME));
        this.service.submit(this);
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
        this.notifyAll();
    }

    public  void unbind(String key,String clientId){
        getSesionGroup(key).remove(clientId);
    }

    public  void publish(String key,byte[] bytes){
        PublishResponse response = new PublishResponse();
        response.setKey(key);
        response.setMessage(bytes);

        byte[] responseBytes =  Serializers.getResponseSerializer().encode(response);
        this.queue.push(responseBytes);
    }

    private synchronized void processNext(){
        try{
            byte[] responseBytes = this.queue.peek();
            String json = new String(responseBytes);
            JSON js = new JSON(json);
            String key = (String)js.get("key");


            Message msg = new Message();
            msg.setBody(responseBytes);

            //获得下个Session
            SessionGroup group = getSesionGroup(key);
            Session session = group.next();
            if(session != null){
                sendResponse(session,msg);
                this.queue.pop();
            }else{
                LOGGER.warn("Not found available poller session.waiting");
                this.wait();
            }
        }catch(Throwable e){
            LOGGER.warn(e.getMessage(),e);
        }
    }

    private void sendResponse(Session session,Message message){
        try{
            session.send(message);
        }catch(Exception e){
            LOGGER.error("error send response.",e);
        }
    }

    @Override
    public void run() {
        while(true){
            processNext();
        }
    }
}
