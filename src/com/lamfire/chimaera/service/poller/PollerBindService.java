package com.lamfire.chimaera.service.poller;

import com.lamfire.chimaera.ChimaeraPoller;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.poller.PollerBindCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.hydra.Session;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.POLLER_BIND)
public class PollerBindService implements Service<PollerBindCommand> {
    static final Logger LOGGER = Logger.getLogger(PollerBindService.class);

    @Override
    public Response execute(MessageContext context, PollerBindCommand cmd) {
        ChimaeraPoller poller = ChimaeraPoller.getInstance();
        String key = cmd.getKey();
        String cid = cmd.getClientId();
        Session session = context.getSession();
        poller.bind(key, cid, session);
        return Responses.makeEmptyResponse(cmd);
    }

}
