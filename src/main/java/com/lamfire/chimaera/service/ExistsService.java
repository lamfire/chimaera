package com.lamfire.chimaera.service;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.ExistsCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.EXISTS)
public class ExistsService implements Service<ExistsCommand> {
    static final Logger LOGGER = Logger.getLogger(ExistsService.class);

    @Override
    public Response execute(MessageContext context, ExistsCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());
        boolean exists = store.exists(cmd.getKey());
        return Responses.makeExistsResponse(cmd, exists);
    }

}
