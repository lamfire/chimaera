package com.lamfire.chimaera.command.set;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name = Command.SET_ADD, writeProtected = true)
public class SetAddCommand extends Command {

    private byte[] value;

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

}
