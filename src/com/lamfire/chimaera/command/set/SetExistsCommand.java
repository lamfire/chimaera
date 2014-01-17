package com.lamfire.chimaera.command.set;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name = Command.SET_EXISTS)
public class SetExistsCommand extends Command {

    private byte[] value;

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
