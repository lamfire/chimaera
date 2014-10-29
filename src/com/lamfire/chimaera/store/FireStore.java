package com.lamfire.chimaera.store;

import java.util.Set;

public interface FireStore {
    public void remove(String key);

    public long count();

    public Set<String> keys();

    public boolean exists(String key);

    public FireIncrement getFireIncrement(String key);

    public FireList getFireList(String key);

    public FireMap getFireMap(String key);

    public FireQueue getFireQueue(String key);

    public FireSet getFireSet(String key);

    public FireRank getFireRank(String key);

}
