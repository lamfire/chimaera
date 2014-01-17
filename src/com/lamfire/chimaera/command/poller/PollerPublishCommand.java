package com.lamfire.chimaera.command.poller;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name = Command.POLLER_PUBLISH)
public class PollerPublishCommand extends Command {
    private byte[] message;
    private String clientId;
    private boolean feedback = false; //是否需要服务器发送一个回执，作为消息反馈，

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }
}
