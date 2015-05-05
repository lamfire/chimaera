package com.lamfire.chimaera.service.rank;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.rank.RankSetCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Pandora;

@SERVICE(command = Command.RANK_SET)
public class RankSetService implements Service<RankSetCommand> {
    static final Logger LOGGER = Logger.getLogger(RankSetService.class);

    @Override
    public Response execute(MessageContext context, RankSetCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());

        store.getFireRank(cmd.getKey()).set(cmd.getName(), cmd.getCount());
        return Responses.makeEmptyResponse(cmd);
    }

}
