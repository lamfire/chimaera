package com.lamfire.chimaera.store;

public interface FireIncrement {

    public void incr();

    public void incr(long step);

    public void decr();

    public void decr(long step);

    public long get();

    public void set(long value);

    public long incrGet();

    public long incrGet(long step);

    public long decrGet();

    public long decrGet(long step);
}
