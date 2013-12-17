package com.lamfire.chimaera.command.set;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name= Command.SET_REMOVE)
public class SetRemoveCommand extends Command{
    private Integer index;
    private byte[] value;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
