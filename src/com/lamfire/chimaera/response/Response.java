package com.lamfire.chimaera.response;

import com.lamfire.chimaera.command.Command;

public abstract class Response extends Command{
	public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_ERROR = -100;

	private int status = STATUS_SUCCESS;
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
