package com.lamfire.chimaera.service.queue;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.command.queue.QueuePopRightCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.QUEUE_POPRIGHT)
public class QueuePopRightService implements Service<QueuePopRightCommand> {
	static final Logger LOGGER = Logger.getLogger(QueuePopRightService.class);

	@Override
	public Response execute(MessageContext context, QueuePopRightCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());
        byte[] bytes = store.getFireQueue(cmd.getKey()).popRight();
        return Responses.makeGetResponse(cmd,bytes);
	}

}
