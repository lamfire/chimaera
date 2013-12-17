package com.lamfire.chimaera.command.increment;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name=Command.INCREMENT_INCR)
public class IncrementIncrCommand extends Command{
    private long step;

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }
}
