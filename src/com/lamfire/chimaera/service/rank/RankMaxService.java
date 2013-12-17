package com.lamfire.chimaera.service.rank;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.store.Item;
import com.lamfire.chimaera.command.rank.RankMaxCommand;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

import java.util.List;

@SERVICE(command = Command.RANK_MAX)
public class RankMaxService implements Service<RankMaxCommand> {
	static final Logger LOGGER = Logger.getLogger(RankMaxService.class);

	@Override
	public Response execute(MessageContext context, RankMaxCommand cmd) {
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        List<Item> list = store.getFireRank(cmd.getKey()).max(cmd.getSize());
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("[MAX] - " + cmd.getKey() + ":size(" +cmd.getSize() + ")");
        }
        return Responses.makeListResponseForCounter(cmd, list);
	}

}
