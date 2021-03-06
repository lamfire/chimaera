package com.lamfire.chimaera.command.poller;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name = Command.POLLER_UNBIND)
public class PollerUnbindCommand extends Command {
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
