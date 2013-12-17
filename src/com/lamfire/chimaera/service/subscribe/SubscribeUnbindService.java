package com.lamfire.chimaera.service.subscribe;

import com.lamfire.chimaera.ChimaeraSubscribe;
import com.lamfire.chimaera.command.subscribe.SubscribeUnbindCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.SUBSCRIBE_UNBIND)
public class SubscribeUnbindService implements Service<SubscribeUnbindCommand> {
	static final Logger LOGGER = Logger.getLogger(SubscribeUnbindService.class);
    private ChimaeraSubscribe subscribe = ChimaeraSubscribe.getInstance();

	@Override
	public Response execute(MessageContext context, SubscribeUnbindCommand cmd) {
		subscribe.unbind(cmd.getKey(), context.getSessionId());
        return Responses.makeEmptyResponse(cmd);
	}

}