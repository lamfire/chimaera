package com.lamfire.chimaera.client;

import com.lamfire.chimaera.ChimaeraException;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.Response;
import com.lamfire.hydra.Session;
import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;

public class ResponseFuture<T extends Response> {
    private static final Logger LOGGER = Logger.getLogger(ResponseFuture.class);
    private int messageId;
    private ResponseWaitQueue waitQueue;
    private long readTimeoutMillis = 30000;
    private T response;
    private Class<?> responseType;
    private Command command;
    private Session session;

    public ResponseFuture(Session session, Command command, int messageId, ResponseWaitQueue waitQueue, Class<?> responseType) {
        this.session = session;
        this.messageId = messageId;
        this.waitQueue = waitQueue;
        this.responseType = responseType;
        this.command = command;
        waitQueue.add(messageId, this);
    }

    public Class<?> getResponseType() {
        return this.responseType;
    }

    public Session getSession() {
        return this.session;
    }

    public long getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(long readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    private void checkResponse() {
        if (this.response == null) {
            String msg = "Read response timeout at:" + this.command.getClass().getSimpleName() + "@" + JSON.toJSONString(this.command);
            LOGGER.error(msg);
            throw new ChimaeraException(msg);
        }
        if (this.response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) this.response;
            throw new ChimaeraException(err.getError());
        }
    }

    public synchronized void await() {
        await(readTimeoutMillis);
    }

    public synchronized void await(long millis) {
        try {
            if (response == null) {
                this.wait(millis);
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ChimaeraException(e.getMessage(), e);
        } finally {
            waitQueue.remove(this.messageId);
        }
    }

    public synchronized T waitResponse() {
        await();
        checkResponse();
        return this.response;
    }

    public T getResponse() {
        return waitResponse();
    }

    synchronized void onResponse(T response) {
        this.response = response;
        this.notifyAll();
    }

}
