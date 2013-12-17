package com.lamfire.chimaera.command.rank;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name= Command.RANK_MAX)
public class RankMaxCommand extends Command{
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
