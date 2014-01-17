package com.lamfire.chimaera.service.queue;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.queue.QueuePopCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.QUEUE_POP)
public class QueuePopService implements Service<QueuePopCommand> {
    static final Logger LOGGER = Logger.getLogger(QueuePopService.class);

    @Override
    public Response execute(MessageContext context, QueuePopCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());
        byte[] bytes = store.getFireQueue(cmd.getKey()).pop();
        return Responses.makeGetResponse(cmd, bytes);
    }

}
