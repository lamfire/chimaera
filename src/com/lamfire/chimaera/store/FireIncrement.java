package com.lamfire.chimaera.store;

public interface FireIncrement extends FireCollection{

    public void incr(String name);

    public void incr(String name,long step);

    public long get(String name);

    public void set(String name,long value);

    public long incrGet(String name);

    public long incrGet(String name,long step);

    public long remove(String name);
}
