package com.lamfire.chimaera.service.list;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.list.ListRemoveCommand;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.LIST_REMOVE)
public class ListRemoveService implements Service<ListRemoveCommand> {
	static final Logger LOGGER = Logger.getLogger(ListRemoveService.class);

	@Override
	public Response execute(MessageContext context, ListRemoveCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        byte[] bytes = store.getFireList(cmd.getKey()).remove(cmd.getIndex());

        return Responses.makeGetResponse(cmd,bytes);
	}

}