package com.lamfire.chimaera.service.list;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.list.ListSizeCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.LIST_SIZE)
public class ListSizeService implements Service<ListSizeCommand> {
    static final Logger LOGGER = Logger.getLogger(ListSizeService.class);

    @Override
    public Response execute(MessageContext context, ListSizeCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        long size = store.getFireList(cmd.getKey()).size();

        return Responses.makeSizeResponse(cmd, size);
    }

}
