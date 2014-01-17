package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireIncrement;

import java.util.concurrent.atomic.AtomicLong;

public class MemoryFireIncrement implements FireIncrement {

    private final AtomicLong atomic = new AtomicLong();

    @Override
    public void incr() {
        atomic.incrementAndGet();
    }

    @Override
    public void incr(long step) {
        if (step < 0) {
            step = Math.abs(step);
        }
        atomic.addAndGet(step);
    }

    @Override
    public void decr() {
        atomic.decrementAndGet();
    }

    @Override
    public void decr(long step) {
        if (step < 0) {
            step = Math.abs(step);
        }
        atomic.addAndGet(0 - step);
    }

    @Override
    public long get() {
        return atomic.get();
    }

    @Override
    public void set(long value) {
        atomic.set(value);
    }

    @Override
    public long incrGet() {
        return atomic.incrementAndGet();
    }

    @Override
    public long incrGet(long step) {
        if (step < 0) {
            step = Math.abs(step);
        }
        return atomic.addAndGet(step);
    }

    @Override
    public long decrGet() {
        return atomic.decrementAndGet();
    }

    @Override
    public long decrGet(long step) {
        if (step < 0) {
            step = Math.abs(step);
        }
        return atomic.addAndGet(0 - step);
    }

}
