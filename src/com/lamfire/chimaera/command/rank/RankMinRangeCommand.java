package com.lamfire.chimaera.command.rank;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name= Command.RANK_MIN_RANGE)
public class RankMinRangeCommand extends Command{
    private int size;
    private int from;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
