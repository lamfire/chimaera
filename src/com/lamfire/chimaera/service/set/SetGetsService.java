package com.lamfire.chimaera.service.set;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.set.SetGetsCommand;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

import java.util.List;

@SERVICE(command = Command.SET_GETS)
public class SetGetsService implements Service<SetGetsCommand> {
	static final Logger LOGGER = Logger.getLogger(SetGetsService.class);

	@Override
	public Response execute(MessageContext context, SetGetsCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        List<byte[]> list = store.getFireSet(cmd.getKey()).gets(cmd.getFromIndex(), cmd.getSize());

		return Responses.makeGetsResponse(cmd,list);
	}

}
