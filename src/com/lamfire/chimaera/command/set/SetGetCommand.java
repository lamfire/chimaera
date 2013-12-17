package com.lamfire.chimaera.command.set;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name=Command.SET_GET)
public class SetGetCommand extends Command{
    private int index;
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
