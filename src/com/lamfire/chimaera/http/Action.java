package com.lamfire.chimaera.http;


public interface Action {

	public byte[] execute(ActionContext context, byte[] message);
	
}
