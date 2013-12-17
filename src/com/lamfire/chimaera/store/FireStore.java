package com.lamfire.chimaera.store;

public interface FireStore {
	public void remove(String key);
	
	public int size(String key);
	
	public void clear(String key);

    public int size();

    public void clear();
	
	public boolean exists(String key);
	
	public  FireIncrement getFireIncrement(String key);

	public  FireList getFireList(String key);
	
	public  FireMap getFireMap(String key);
	
	public  FireQueue getFireQueue(String key);
	
	public  FireSet getFireSet(String key);

    public FireRank getFireRank(String key);

}
