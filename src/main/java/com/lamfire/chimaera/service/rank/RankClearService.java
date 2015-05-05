package com.lamfire.chimaera.service.rank;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.rank.RankClearCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.RANK_CLEAR)
public class RankClearService implements Service<RankClearCommand> {
    static final Logger LOGGER = Logger.getLogger(RankClearService.class);

    @Override
    public Response execute(MessageContext context, RankClearCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        store.getFireRank(cmd.getKey()).clear();
        return Responses.makeClearResponse(cmd);
    }

}
