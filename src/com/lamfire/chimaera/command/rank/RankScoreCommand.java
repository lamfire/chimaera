package com.lamfire.chimaera.command.rank;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name= Command.RANK_SCORE)
public class RankScoreCommand extends Command{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
