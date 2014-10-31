package com.lamfire.chimaera.client;

import com.lamfire.chimaera.OnMessageListener;
import com.lamfire.hydra.Session;
import com.lamfire.logger.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-1-16
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
public class BindTask {
    private static final Logger LOGGER = Logger.getLogger(BindTask.class);
    private String key;
    private String clientId;
    private OnMessageListener listener;
    private Session session;
    private BindInterface bind;

    public BindTask(BindInterface bind) {
        this.bind = bind;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public OnMessageListener getListener() {
        return listener;
    }

    public void setListener(OnMessageListener listener) {
        this.listener = listener;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isAvailable() {
        return this.session.isConnected();
    }

    public void rebind() {
        try {
            if (this.session.isClosed()) {
                this.session = this.bind.rebind(this.key, this.clientId, this.listener);
                LOGGER.info("rebind successfully," + this);
            }
        } catch (Throwable t) {
            LOGGER.warn("rebind failed," + t.getMessage(), t);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "session=" + session +
                ", clientId='" + clientId + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
