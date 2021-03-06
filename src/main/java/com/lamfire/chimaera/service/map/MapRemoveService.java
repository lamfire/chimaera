package com.lamfire.chimaera.service.map;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.map.MapRemoveCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.MAP_REMOVE)
public class MapRemoveService implements Service<MapRemoveCommand> {
    static final Logger LOGGER = Logger.getLogger(MapRemoveService.class);

    @Override
    public Response execute(MessageContext context, MapRemoveCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        store.getFireMap(cmd.getKey()).remove(cmd.getField());
        return Responses.makeEmptyResponse(cmd);
    }

}
