package com.lamfire.chimaera.service.map;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.map.MapPutCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.MAP_PUT)
public class MapPutService implements Service<MapPutCommand> {
    static final Logger LOGGER = Logger.getLogger(MapPutService.class);

    @Override
    public Response execute(MessageContext context, MapPutCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        store.getFireMap(cmd.getKey()).put(cmd.getField(), cmd.getValue());
        return Responses.makeEmptyResponse(cmd);
    }

}
