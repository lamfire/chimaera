package com.lamfire.chimaera.service.list;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.list.ListGetCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.LIST_GET)
public class ListGetService implements Service<ListGetCommand> {
    static final Logger LOGGER = Logger.getLogger(ListGetService.class);

    @Override
    public Response execute(MessageContext context, ListGetCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        byte[] bytes = store.getFireList(cmd.getKey()).get(cmd.getIndex());
        return Responses.makeGetResponse(cmd, bytes);
    }

}
