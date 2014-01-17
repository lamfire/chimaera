package com.lamfire.chimaera.service.rank;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.rank.RankSizeCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.RANK_SIZE)
public class RankSizeService implements Service<RankSizeCommand> {
    static final Logger LOGGER = Logger.getLogger(RankSizeService.class);

    @Override
    public Response execute(MessageContext context, RankSizeCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        int size = store.getFireRank(cmd.getKey()).size();
        return Responses.makeSizeResponse(cmd, size);
    }

}
