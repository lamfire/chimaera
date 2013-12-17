package com.lamfire.chimaera.service.set;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.command.set.SetExistsCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.SET_EXISTS)
public class SetExistsService implements Service<SetExistsCommand> {
	static final Logger LOGGER = Logger.getLogger(SetExistsService.class);

	@Override
	public Response execute(MessageContext context, SetExistsCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        byte[] value = cmd.getValue();
        boolean exists = store.getFireSet(cmd.getKey()).exists(value);

        return Responses.makeExistsResponse(cmd,exists);
	}

}
