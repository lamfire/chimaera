package com.lamfire.chimaera.service;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.SizeCommand;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;
import com.lamfire.utils.StringUtils;

@SERVICE(command = Command.SIZE)
public class SizeService implements Service<SizeCommand> {
	static final Logger LOGGER = Logger.getLogger(SizeService.class);

	@Override
	public Response execute(MessageContext context, SizeCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        int size = 0;
        if(StringUtils.isBlank(cmd.getKey())){
            size = store.size();
        }  else{
            size = store.size(cmd.getKey());
        }
		return Responses.makeSizeResponse(cmd,size);
	}

}
