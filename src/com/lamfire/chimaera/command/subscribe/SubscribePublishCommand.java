package com.lamfire.chimaera.command.subscribe;

import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.command.Command;

@COMMAND(name=Command.SUBSCRIBE_PUBLISH)
public class SubscribePublishCommand extends Command{
	private byte[] message;

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}
}
