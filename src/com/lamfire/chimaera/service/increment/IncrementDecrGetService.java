package com.lamfire.chimaera.service.increment;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.command.increment.IncrementDecrGetCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.INCREMENT_DECR_GET)
public class IncrementDecrGetService implements Service<IncrementDecrGetCommand> {
	static final Logger LOGGER = Logger.getLogger(IncrementDecrGetService.class);

	@Override
	public Response execute(MessageContext context, IncrementDecrGetCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        long step = cmd.getStep();
        long val = store.getFireIncrement(cmd.getKey()).decrGet(step);
        return Responses.makeIncrGetResponse(cmd,val);
	}

}
