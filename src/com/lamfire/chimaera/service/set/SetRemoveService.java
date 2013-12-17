package com.lamfire.chimaera.service.set;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.set.SetRemoveCommand;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.SET_REMOVE)
public class SetRemoveService implements Service<SetRemoveCommand> {
	static final Logger LOGGER = Logger.getLogger(SetRemoveService.class);

	@Override
	public Response execute(MessageContext context, SetRemoveCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());
        Integer index = cmd.getIndex();
        byte[] value = cmd.getValue();
        if(index == null && value == null){
            ErrorResponse err = new ErrorResponse();
            err.setError("The parameters 'index' or 'value' cannot both blank");
            return err;
        }
        if(index != null){
            value = store.getFireSet(cmd.getKey()).remove(index);
        } else{
            store.getFireSet(cmd.getKey()).remove(value);
        }

        return Responses.makeGetResponse(cmd,value);
	}

}
