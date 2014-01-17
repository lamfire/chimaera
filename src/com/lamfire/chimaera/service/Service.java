package com.lamfire.chimaera.service;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.Response;
import com.lamfire.hydra.MessageContext;

public interface Service<E extends Command> {

    public Response execute(MessageContext context, E command);
}
