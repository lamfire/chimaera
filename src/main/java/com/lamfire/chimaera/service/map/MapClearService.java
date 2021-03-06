package com.lamfire.chimaera.service.map;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.map.MapClearCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.MAP_CLEAR)
public class MapClearService implements Service<MapClearCommand> {
    static final Logger LOGGER = Logger.getLogger(MapClearService.class);

    @Override
    public Response execute(MessageContext context, MapClearCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        store.getFireMap(cmd.getKey()).clear();
        return Responses.makeEmptyResponse(cmd);
    }

}
