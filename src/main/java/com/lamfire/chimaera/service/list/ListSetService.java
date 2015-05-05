package com.lamfire.chimaera.service.list;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.list.ListSetCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.LIST_SET)
public class ListSetService implements Service<ListSetCommand> {
    static final Logger LOGGER = Logger.getLogger(ListSetService.class);

    @Override
    public Response execute(MessageContext context, ListSetCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        store.getFireList(cmd.getKey()).set(cmd.getIndex(), cmd.getValue());
        return Responses.makeEmptyResponse(cmd);
    }

}
