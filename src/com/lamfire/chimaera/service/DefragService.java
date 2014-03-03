package com.lamfire.chimaera.service;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.DefragCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.DEFRAGE)
public class DefragService implements Service<DefragCommand> {
    static final Logger LOGGER = Logger.getLogger(DefragService.class);

    @Override
    public Response execute(MessageContext context, DefragCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());
        store.defrag();
        return Responses.makeEmptyResponse(cmd);
    }

}
