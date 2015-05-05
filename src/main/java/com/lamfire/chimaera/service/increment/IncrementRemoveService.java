package com.lamfire.chimaera.service.increment;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.increment.IncrementRemoveCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.INCREMENT_REMOVE)
public class IncrementRemoveService implements Service<IncrementRemoveCommand> {
    static final Logger LOGGER = Logger.getLogger(IncrementRemoveService.class);

    @Override
    public Response execute(MessageContext context, IncrementRemoveCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());
        long val = store.getFireIncrement(cmd.getKey()).remove(cmd.getName());
        return Responses.makeIncrGetResponse(cmd,val);
    }

}
