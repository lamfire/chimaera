package com.lamfire.chimaera.service.map;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.map.MapKeysCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.chimaera.response.ErrorResponse;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.logger.Logger;

import java.util.List;

@SERVICE(command = Command.MAP_KEYS)
public class MapKeysService implements Service<MapKeysCommand> {
	static final Logger LOGGER = Logger.getLogger(MapKeysService.class);
	@Override
	public Response execute(MessageContext context, MapKeysCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        if(!store.exists(cmd.getKey())){
            ErrorResponse err = new ErrorResponse();
            err.setError("The key["+cmd.getKey()+"] not exists on store");
            return err;
        }
        List<String> keys = store.getFireMap(cmd.getKey()).keys();
        return Responses.makeMapKeysResponse(cmd, keys);
	}

}
