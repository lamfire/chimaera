package com.lamfire.chimaera.service.poller;

import com.lamfire.chimaera.ChimaeraPoller;
import com.lamfire.chimaera.ChimaeraSubscribe;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.poller.PollerUnbindCommand;
import com.lamfire.chimaera.command.subscribe.SubscribeUnbindCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.POLLER_UNBIND)
public class PollerUnbindService implements Service<PollerUnbindCommand> {
	static final Logger LOGGER = Logger.getLogger(PollerUnbindService.class);


	@Override
	public Response execute(MessageContext context, PollerUnbindCommand cmd) {
        ChimaeraPoller poller = ChimaeraPoller.getInstance();
        poller.unbind(cmd.getKey(),cmd.getClientId());
        return Responses.makeEmptyResponse(cmd);
	}

}
