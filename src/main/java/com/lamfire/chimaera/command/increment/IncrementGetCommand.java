package com.lamfire.chimaera.command.increment;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name = Command.INCREMENT_GET)
public class IncrementGetCommand extends Command {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
