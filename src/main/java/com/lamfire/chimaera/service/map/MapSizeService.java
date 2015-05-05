package com.lamfire.chimaera.service.map;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.map.MapSizeCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.MAP_SIZE)
public class MapSizeService implements Service<MapSizeCommand> {
    static final Logger LOGGER = Logger.getLogger(MapSizeService.class);

    @Override
    public Response execute(MessageContext context, MapSizeCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        long size = store.getFireMap(cmd.getKey()).size();
        return Responses.makeSizeResponse(cmd, size);
    }

}
