package com.lamfire.chimaera.service.list;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.list.ListGetsCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

import java.util.List;

@SERVICE(command = Command.LIST_GETS)
public class ListGetsService implements Service<ListGetsCommand> {
    static final Logger LOGGER = Logger.getLogger(ListGetsService.class);

    @Override
    public Response execute(MessageContext context, ListGetsCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        List<byte[]> list = store.getFireList(cmd.getKey()).gets(cmd.getFromIndex(), cmd.getSize());
        return Responses.makeGetsResponse(cmd, list);
    }

}
