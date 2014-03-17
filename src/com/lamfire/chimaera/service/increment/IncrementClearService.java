package com.lamfire.chimaera.service.increment;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.increment.IncrementClearCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.INCREMENT_CLEAR)
public class IncrementClearService implements Service<IncrementClearCommand> {
    static final Logger LOGGER = Logger.getLogger(IncrementClearService.class);

    @Override
    public Response execute(MessageContext context, IncrementClearCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());
        store.getFireIncrement(cmd.getKey()).clear();
        return Responses.makeEmptyResponse(cmd);
    }

}
