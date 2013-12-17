package com.lamfire.chimaera.service.rank;

import com.lamfire.chimaera.Chimaera;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.chimaera.response.Response;
import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.command.rank.RankIncrCommand;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.response.Responses;
import com.lamfire.chimaera.store.FireStore;
import com.lamfire.logger.Logger;
import com.lamfire.hydra.MessageContext;

@SERVICE(command = Command.RANK_INCR)
public class RankIncrService implements Service<RankIncrCommand> {
	static final Logger LOGGER = Logger.getLogger(RankIncrService.class);

	@Override
	public Response execute(MessageContext context, RankIncrCommand command) {
		RankIncrCommand cmd = (RankIncrCommand) command;
		FireStore store = Chimaera.getFireStore(cmd.getStore());

        store.getFireRank(cmd.getKey()).incr(cmd.getName(), cmd.getStep());
        return Responses.makeEmptyResponse(cmd);
	}

}
