package com.lamfire.chimaera.service.increment;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.increment.IncrementIncrCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.INCREMENT_INCR)
public class IncrementIncrService implements Service<IncrementIncrCommand> {
    static final Logger LOGGER = Logger.getLogger(IncrementIncrService.class);

    @Override
    public Response execute(MessageContext context, IncrementIncrCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        long step = cmd.getStep();
        store.getFireIncrement(cmd.getKey()).incr(cmd.getName(),step);
        return Responses.makeEmptyResponse(cmd);
    }

}
