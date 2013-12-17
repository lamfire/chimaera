package com.lamfire.chimaera.store;

public interface FireQueue extends FireCollection{
	
	public void pushLeft(byte[] value);
	
	public byte[] popLeft();
	
	public void pushRight(byte[] value);
	
	public byte[] popRight();
	
}
