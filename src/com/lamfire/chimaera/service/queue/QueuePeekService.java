package com.lamfire.chimaera.service.queue;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.queue.QueuePeekCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.QUEUE_PEEK)
public class QueuePeekService implements Service<QueuePeekCommand> {
    static final Logger LOGGER = Logger.getLogger(QueuePeekService.class);

    @Override
    public Response execute(MessageContext context, QueuePeekCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());
        byte[] bytes = store.getFireQueue(cmd.getKey()).peek();
        return Responses.makeGetResponse(cmd, bytes);
    }

}
