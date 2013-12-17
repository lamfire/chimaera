package com.lamfire.chimaera.command.rank;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name= Command.RANK_INCR,writeProtected = true)
public class RankIncrCommand extends Command{

	private String name;
    private long step;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }
}
