package com.lamfire.chimaera.service;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.RemoveCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.REMOVE)
public class RemoveService implements Service<RemoveCommand> {
    static final Logger LOGGER = Logger.getLogger(RemoveService.class);

    @Override
    public Response execute(MessageContext context, RemoveCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());
        store.remove(cmd.getKey());
        return Responses.makeEmptyResponse(cmd);
    }

}
