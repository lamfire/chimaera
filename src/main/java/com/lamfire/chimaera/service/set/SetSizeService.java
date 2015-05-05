package com.lamfire.chimaera.service.set;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.set.SetSizeCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.SET_SIZE)
public class SetSizeService implements Service<SetSizeCommand> {
    static final Logger LOGGER = Logger.getLogger(SetSizeService.class);

    @Override
    public Response execute(MessageContext context, SetSizeCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        long size = store.getFireSet(cmd.getKey()).size();

        return Responses.makeSizeResponse(cmd, size);
    }

}
