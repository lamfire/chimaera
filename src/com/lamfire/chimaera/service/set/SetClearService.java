package com.lamfire.chimaera.service.set;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.set.SetClearCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.SET_CLEAR)
public class SetClearService implements Service<SetClearCommand> {
    static final Logger LOGGER = Logger.getLogger(SetClearService.class);

    @Override
    public Response execute(MessageContext context, SetClearCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        store.getFireSet(cmd.getKey()).clear();
        return Responses.makeClearResponse(cmd);
    }

}
