package com.lamfire.chimaera.command.map;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name= Command.MAP_PUT,writeProtected = true)
public class MapPutCommand extends Command{
    private String field;

	private byte[] value;

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
