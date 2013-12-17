package com.lamfire.chimaera.service.map;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.chimaera.command.map.MapGetCommand;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.MAP_GET)
public class MapGetService implements Service<MapGetCommand> {
	static final Logger LOGGER = Logger.getLogger(MapGetService.class);
	@Override
	public Response execute(MessageContext context, MapGetCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        if(!store.exists(cmd.getKey())){
            ErrorResponse err = new ErrorResponse();
            err.setError("The key["+cmd.getKey()+"] not exists on store");
            return err;
        }
        byte[] bytes = store.getFireMap(cmd.getKey()).get(cmd.getField());

        return Responses.makeGetResponse(cmd, bytes);
	}

}
