package com.lamfire.chimaera.command.queue;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name=Command.QUEUE_PUSHRIGHT,writeProtected = true)
public class QueuePushRightCommand extends Command{

	private byte[] value;

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
	
}
