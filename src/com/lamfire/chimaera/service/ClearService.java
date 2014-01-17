package com.lamfire.chimaera.service;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.ClearCommand;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.utils.StringUtils;

@SERVICE(command = Command.CLEAR)
public class ClearService implements Service<ClearCommand> {
    static final Logger LOGGER = Logger.getLogger(ClearService.class);

    @Override
    public Response execute(MessageContext context, ClearCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        if (StringUtils.isBlank(cmd.getKey())) {
            store.clear();
        } else {
            store.clear(cmd.getKey());
        }
        return Responses.makeClearResponse(cmd);

    }

}
