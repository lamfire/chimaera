package com.lamfire.chimaera.service;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.ClearCommand;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.KeysCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.utils.StringUtils;

@SERVICE(command = Command.KEYS)
public class KeysService implements Service<KeysCommand> {
    static final Logger LOGGER = Logger.getLogger(KeysService.class);

    @Override
    public Response execute(MessageContext context, KeysCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());
        return Responses.makeKeysResponse(cmd,store.keys());
    }

}
