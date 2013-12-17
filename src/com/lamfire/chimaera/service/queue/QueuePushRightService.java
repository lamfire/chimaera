package com.lamfire.chimaera.service.queue;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.queue.QueuePushRightCommand;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.QUEUE_PUSHRIGHT)
public class QueuePushRightService implements Service <QueuePushRightCommand>{
	static final Logger LOGGER = Logger.getLogger(QueuePushRightService.class);

	@Override
	public Response execute(MessageContext context, QueuePushRightCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        store.getFireQueue(cmd.getKey()).pushRight(cmd.getValue());
        return Responses.makeEmptyResponse(cmd);
	}

}
