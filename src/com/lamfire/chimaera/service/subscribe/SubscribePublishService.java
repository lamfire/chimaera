package com.lamfire.chimaera.service.subscribe;

import com.lamfire.chimaera.ChimaeraSubscribe;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.subscribe.SubscribePublishCommand;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.SUBSCRIBE_PUBLISH)
public class SubscribePublishService implements Service<SubscribePublishCommand> {
	static final Logger LOGGER = Logger.getLogger(SubscribePublishService.class);
    private ChimaeraSubscribe subscribe = ChimaeraSubscribe.getInstance();

	@Override
	public Response execute(MessageContext context, SubscribePublishCommand cmd) {
		subscribe.publish(cmd.getKey(),context.getSessionId(),cmd.getMessage());
        return Responses.makeEmptyResponse(cmd);
	}

}
