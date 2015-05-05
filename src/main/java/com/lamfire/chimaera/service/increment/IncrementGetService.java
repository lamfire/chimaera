package com.lamfire.chimaera.service.increment;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.increment.IncrementGetCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.INCREMENT_GET)
public class IncrementGetService implements Service<IncrementGetCommand> {
    static final Logger LOGGER = Logger.getLogger(IncrementGetService.class);

    @Override
    public Response execute(MessageContext context, IncrementGetCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        long val = store.getFireIncrement(cmd.getKey()).get(cmd.getName());
        return Responses.makeIncrGetResponse(cmd, val);

    }

}
