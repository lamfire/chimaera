package com.lamfire.chimaera.command.subscribe;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name = Command.SUBSCRIBE_BIND)
public class SubscribeBindCommand extends Command {
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
