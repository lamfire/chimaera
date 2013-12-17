package com.lamfire.chimaera.service.queue;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.queue.QueuePopLeftCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.QUEUE_POPLEFT)
public class QueuePopLeftService implements Service<QueuePopLeftCommand> {
	static final Logger LOGGER = Logger.getLogger(QueuePopLeftService.class);

	@Override
	public Response execute(MessageContext context, QueuePopLeftCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        byte[] bytes = store.getFireQueue(cmd.getKey()).popLeft();
        return Responses.makeGetResponse(cmd, bytes);

	}

}
