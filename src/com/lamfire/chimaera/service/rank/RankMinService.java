package com.lamfire.chimaera.service.rank;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.rank.RankMinCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.chimaera.store.Item;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;

import java.util.List;

@SERVICE(command = Command.RANK_MIN)
public class RankMinService implements Service<RankMinCommand> {
    static final Logger LOGGER = Logger.getLogger(RankMinService.class);

    @Override
    public Response execute(MessageContext context, RankMinCommand cmd) {
        FireStore store = Chimaera.getFireStore(cmd.getStore());
        List<Item> list = store.getFireRank(cmd.getKey()).min(cmd.getSize());
        return Responses.makeListResponseForCounter(cmd, list);
    }

}
