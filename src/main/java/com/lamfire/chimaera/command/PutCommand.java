package com.lamfire.chimaera.command;

import com.lamfire.chimaera.annotation.COMMAND;

@COMMAND(name = Command.PUT, writeProtected = true)
public class PutCommand extends Command {

    private byte[] value;

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
