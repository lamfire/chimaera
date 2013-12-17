package com.lamfire.chimaera.service.list;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.command.list.ListAddCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.LIST_ADD)
public class ListAddService implements Service<ListAddCommand> {
	static final Logger LOGGER = Logger.getLogger(ListAddService.class);

	@Override
	public Response execute(MessageContext context, ListAddCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        store.getFireList(cmd.getKey()).add(cmd.getValue());
        return Responses.makeEmptyResponse(cmd);

	}

}
