package com.lamfire.chimaera.service;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.CountCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.utils.StringUtils;

@SERVICE(command = Command.COUNT)
public class CountService implements Service<CountCommand> {
    static final Logger LOGGER = Logger.getLogger(CountService.class);

    @Override
    public Response execute(MessageContext context, CountCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        long size = 0;
        if (StringUtils.isBlank(cmd.getKey())) {
            size = store.count();
        }
        return Responses.makeSizeResponse(cmd, size);
    }

}
