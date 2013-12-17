package com.lamfire.chimaera.command.queue;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name=Command.QUEUE_PUSHLEFT,writeProtected = true)
public class QueuePushLeftCommand extends Command{

	private byte[] value;

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
	
}
