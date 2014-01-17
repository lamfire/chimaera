package com.lamfire.chimaera.command.list;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name = Command.LIST_ADD, writeProtected = true)
public class ListAddCommand extends Command {

    private byte[] value;

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

}
