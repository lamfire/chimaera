package com.lamfire.chimaera.service.queue;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.queue.QueueSizeCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.QUEUE_SIZE)
public class QueueSizeService implements Service<QueueSizeCommand> {
	static final Logger LOGGER = Logger.getLogger(QueueSizeService.class);

	@Override
	public Response execute(MessageContext context, QueueSizeCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        int  size = store.getFireQueue(cmd.getKey()).size();
        return Responses.makeSizeResponse(cmd,size);
	}

}
