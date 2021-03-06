package com.lamfire.chimaera.service.map;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.map.MapExistsCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.MAP_EXISTS)
public class MapExistsService implements Service<MapExistsCommand> {
    static final Logger LOGGER = Logger.getLogger(MapExistsService.class);

    @Override
    public Response execute(MessageContext context, MapExistsCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        boolean exists = store.getFireMap(cmd.getKey()).exists(cmd.getField());

        return Responses.makeExistsResponse(cmd, exists);
    }

}
