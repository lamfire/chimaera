package com.lamfire.chimaera.store;

public interface FireQueue extends FireCollection{
	
	public void push(byte[] value);
	
	public byte[] pop();

}
