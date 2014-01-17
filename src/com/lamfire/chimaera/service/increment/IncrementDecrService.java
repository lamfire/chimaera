package com.lamfire.chimaera.service.increment;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.increment.IncrementDecrCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.INCREMENT_DECR)
public class IncrementDecrService implements Service<IncrementDecrCommand> {
    static final Logger LOGGER = Logger.getLogger(IncrementDecrService.class);

    @Override
    public Response execute(MessageContext context, IncrementDecrCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        long step = cmd.getStep();
        store.getFireIncrement(cmd.getKey()).decr(step);
        return Responses.makeEmptyResponse(cmd);

    }

}
