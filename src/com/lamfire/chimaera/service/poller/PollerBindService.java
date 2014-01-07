package com.lamfire.chimaera.service.poller;

import com.lamfire.chimaera.ChimaeraPoller;
import com.lamfire.chimaera.ChimaeraSubscribe;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.poller.PollerBindCommand;
import com.lamfire.chimaera.command.subscribe.SubscribeBindCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.POLLER_BIND)
public class PollerBindService implements Service<PollerBindCommand> {
	static final Logger LOGGER = Logger.getLogger(PollerBindService.class);
    private ChimaeraPoller poller = ChimaeraPoller.getInstance();

	@Override
	public Response execute(MessageContext context, PollerBindCommand cmd) {
        poller.bind(cmd.getKey(),cmd.getClientId(),context.getSession());
        return Responses.makeEmptyResponse(cmd);
	}

}
