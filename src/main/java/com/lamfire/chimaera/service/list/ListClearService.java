package com.lamfire.chimaera.service.list;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.list.ListClearCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.LIST_CLEAR)
public class ListClearService implements Service<ListClearCommand> {
    static final Logger LOGGER = Logger.getLogger(ListClearService.class);

    @Override
    public Response execute(MessageContext context, ListClearCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        store.getFireList(cmd.getKey()).clear();
        return Responses.makeClearResponse(cmd);
    }

}
