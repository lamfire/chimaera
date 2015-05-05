package com.lamfire.chimaera.service.rank;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.rank.RankMinRangeCommand;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.hydra.MessageContext;
import com.lamfire.logger.Logger;
import com.lamfire.pandora.Item;
import com.lamfire.pandora.Pandora;

import java.util.List;

@SERVICE(command = Command.RANK_MIN_RANGE)
public class RankMinRangeService implements Service<RankMinRangeCommand> {
    static final Logger LOGGER = Logger.getLogger(RankMinRangeService.class);

    @Override
    public Response execute(MessageContext context, RankMinRangeCommand cmd) {
        Pandora store = Chimaera.getPandora(cmd.getStore());
        List<Item> list = store.getFireRank(cmd.getKey()).minRange(cmd.getFrom(), cmd.getSize());
        return Responses.makeListResponseForCounter(cmd, list);
    }

}
