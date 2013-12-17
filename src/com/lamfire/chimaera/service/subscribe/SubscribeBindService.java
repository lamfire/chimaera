package com.lamfire.chimaera.service.subscribe;

import com.lamfire.chimaera.ChimaeraSubscribe;
import com.lamfire.chimaera.command.subscribe.SubscribeBindCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.SUBSCRIBE_BIND)
public class SubscribeBindService implements Service<SubscribeBindCommand> {
	static final Logger LOGGER = Logger.getLogger(SubscribeBindService.class);
    private ChimaeraSubscribe subscribe = ChimaeraSubscribe.getInstance();

	@Override
	public Response execute(MessageContext context, SubscribeBindCommand cmd) {
		subscribe.bind(cmd.getKey(),context.getSession());
        return Responses.makeEmptyResponse(cmd);
	}

}
