package com.lamfire.chimaera.service.poller;

import com.lamfire.chimaera.ChimaeraPoller;
import com.lamfire.chimaera.ChimaeraSubscribe;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.poller.PollerPublishCommand;
import com.lamfire.chimaera.command.subscribe.SubscribePublishCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.POLLER_PUBLISH)
public class PollerPublishService implements Service<PollerPublishCommand> {
	static final Logger LOGGER = Logger.getLogger(PollerPublishService.class);

	@Override
	public Response execute(MessageContext context, PollerPublishCommand cmd) {
        ChimaeraPoller poller = ChimaeraPoller.getInstance();
        poller.publish(cmd.getKey(),cmd.getMessage());
        if(cmd.isFeedback()){
            return Responses.makeEmptyResponse(cmd);
        }
        return null;
	}

}
