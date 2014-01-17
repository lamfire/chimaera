package com.lamfire.chimaera.service.rank;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.rank.RankClearCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.RANK_CLEAR)
public class RankClearService implements Service<RankClearCommand> {
    static final Logger LOGGER = Logger.getLogger(RankClearService.class);

    @Override
    public Response execute(MessageContext context, RankClearCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        store.getFireRank(cmd.getKey()).clear();
        return Responses.makeClearResponse(cmd);
    }

}
