package com.lamfire.chimaera.service.increment;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.increment.IncrementIncrGetCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.INCREMENT_INCR_GET)
public class IncrementIncrGetService implements Service<IncrementIncrGetCommand> {
    static final Logger LOGGER = Logger.getLogger(IncrementIncrGetService.class);

    @Override
    public Response execute(MessageContext context, IncrementIncrGetCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        long step = cmd.getStep();
        long val = store.getFireIncrement(cmd.getKey()).incrGet(cmd.getName(),step);
        return Responses.makeIncrGetResponse(cmd, val);
    }

}
