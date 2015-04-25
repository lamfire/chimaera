package com.lamfire.chimaera.service.queue;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.queue.QueueClearCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.QUEUE_CLEAR)
public class QueueClearService implements Service<QueueClearCommand> {
    static final Logger LOGGER = Logger.getLogger(QueueClearService.class);

    @Override
    public Response execute(MessageContext context, QueueClearCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        store.getFireQueue(cmd.getKey()).clear();
        return Responses.makeClearResponse(cmd);
    }

}
