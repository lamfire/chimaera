package com.lamfire.chimaera.service.rank;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.rank.RankScoreCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

@SERVICE(command = Command.RANK_SCORE)
public class RankScoreService implements Service<RankScoreCommand> {
    static final Logger LOGGER = Logger.getLogger(RankScoreService.class);

    @Override
    public Response execute(MessageContext context, RankScoreCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());

        long count = store.getFireRank(cmd.getKey()).score(cmd.getName());
        return Responses.makeCountResponseForCounter(cmd, cmd.getName(), count);
    }

}
