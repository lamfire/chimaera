package com.lamfire.chimaera.service.queue;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.queue.QueuePushCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.QUEUE_PUSH)
public class QueuePushService implements Service<QueuePushCommand> {
    static final Logger LOGGER = Logger.getLogger(QueuePushService.class);

    @Override
    public Response execute(MessageContext context, QueuePushCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());
        store.getFireQueue(cmd.getKey()).push(cmd.getValue());
        return Responses.makeEmptyResponse(cmd);
    }

}
