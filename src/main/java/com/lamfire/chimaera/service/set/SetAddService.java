package com.lamfire.chimaera.service.set;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.set.SetAddCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.SET_ADD)
public class SetAddService implements Service<SetAddCommand> {
    static final Logger LOGGER = Logger.getLogger(SetAddService.class);

    @Override
    public Response execute(MessageContext context, SetAddCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        store.getFireSet(cmd.getKey()).add(cmd.getValue());
        return Responses.makeEmptyResponse(cmd);
    }

}
