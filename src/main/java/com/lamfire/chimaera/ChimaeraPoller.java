package com.lamfire.chimaera;

import com.lamfire.chimaera.queue.BlockingQueue;
import com.lamfire.chimaera.queue.PersistentQueue;
import com.lamfire.chimaera.response.subscribe.PublishResponse;
import com.lamfire.chimaera.serializer.Serializers;
import com.lamfire.hydra.Message;
import com.lamfire.hydra.Session;
import com.lamfire.hydra.SessionGroup;
import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Maps;
import com.lamfire.utils.Threads;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-7
 * Time: 下午7:20
 * To change this template use File | Settings | File Templates.
 */
public class ChimaeraPoller {
    private static final Logger LOGGER = Logger.getLogger(ChimaeraPoller.class);
    private final Map<String, SessionGroup> groups = Maps.newHashMap();
    private static final ChimaeraPoller instance = new ChimaeraPoller();
    public static final String STORE_NAME = "CHIMAERA_POLLER";
    public static final String QUEUE_NAME = "CHIMAERA_POLLER_QUEUE";


    public static final ChimaeraPoller getInstance() {
        return instance;
    }

    private Runnable executor = new Runnable() {
        @Override
        public void run() {
            while (true) {
                dispatchNextMessage();
            }
        }
    };

    private BlockingQueue queue;   //消息队列
    private ExecutorService service; //消息分发服务
    private Lock lock = new ReentrantLock();

    private ChimaeraPoller() {
        PersistentQueue fireQueue = Chimaera.makePersistentQueue(STORE_NAME);
        try {
            fireQueue.open();
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(),e);
        }
        this.queue = new BlockingQueue(fireQueue);
        this.service = Executors.newFixedThreadPool(1, Threads.makeThreadFactory(STORE_NAME));
        this.service.submit(executor);
    }

    private synchronized SessionGroup newSessionGroup(String key) {
        SessionGroup group = groups.get(key);
        if (group == null) {
            group = new SessionGroup();
            groups.put(key, group);
        }
        return group;
    }

    public SessionGroup getSesionGroup(String key) {
        SessionGroup group = groups.get(key);
        if (group == null) {
            return newSessionGroup(key);
        }
        return group;
    }

    public void bind(String key, String clientId, Session session) {
        try {
            SessionGroup group = getSesionGroup(key);
            if (group.exists(clientId)) {
                Session old = group.remove(clientId);
                LOGGER.info("[" + clientId + "] change[" + old + "] to[" + session + "]");
            }

            group.add(clientId, session);
            LOGGER.info("add bind [" + key + " " + clientId + "] to[" + session + "]");

            synchronized (lock) {
                lock.notifyAll();
            }
        } catch (Throwable t) {
            LOGGER.error(t.getMessage(), t);
        }

    }

    public void unbind(String key, String clientId) {
        getSesionGroup(key).remove(clientId);
    }

    public void publish(String key, byte[] bytes) {
        PublishResponse response = new PublishResponse();
        response.setKey(key);
        response.setMessage(bytes);
        response.setType(PublishResponse.TYPE_POLLER);

        byte[] responseBytes = Serializers.getResponseSerializer().encode(response);
        this.queue.push(responseBytes);
    }

    private void dispatchNextMessage() {
        try {
            byte[] responseBytes = this.queue.peek();
            String json = new String(responseBytes);
            JSON js = JSON.fromJSONString(json);
            String key = (String) js.get("key");

            //获得下个Session
            SessionGroup group = getSesionGroup(key);
            Session session = group.getPollingNextSession();
            if (session != null) {
                Message msg = new Message();
                msg.setBody(responseBytes);
                session.send(msg).sync();
                this.queue.pop();
            } else {
                LOGGER.warn("Not found available poller session,waiting...");
                synchronized (lock) {
                    lock.wait();
                }
            }
        } catch (Throwable e) {
            LOGGER.warn("[Dispatch Message Failed]:"+e.getMessage());
        }
    }

}
