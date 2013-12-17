package com.lamfire.chimaera.service.increment;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.increment.IncrementSetCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.INCREMENT_SET)
public class IncrementSetService implements Service<IncrementSetCommand> {
	static final Logger LOGGER = Logger.getLogger(IncrementSetService.class);

	@Override
	public Response execute(MessageContext context, IncrementSetCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        store.getFireIncrement(cmd.getKey()).set(cmd.getValue());
        return Responses.makeEmptyResponse(cmd);

	}

}
