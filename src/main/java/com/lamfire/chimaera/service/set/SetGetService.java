package com.lamfire.chimaera.service.set;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.set.SetGetCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.SET_GET)
public class SetGetService implements Service<SetGetCommand> {
    static final Logger LOGGER = Logger.getLogger(SetGetService.class);

    @Override
    public Response execute(MessageContext context, SetGetCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        byte[] bytes = store.getFireSet(cmd.getKey()).get(cmd.getIndex());

        return Responses.makeGetResponse(cmd, bytes);
    }

}
