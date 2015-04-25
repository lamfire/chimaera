package com.lamfire.chimaera.service.map;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.map.MapSizeCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.MAP_SIZE)
public class MapSizeService implements Service<MapSizeCommand> {
    static final Logger LOGGER = Logger.getLogger(MapSizeService.class);

    @Override
    public Response execute(MessageContext context, MapSizeCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        long size = store.getFireMap(cmd.getKey()).size();
        return Responses.makeSizeResponse(cmd, size);
    }

}
