package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.utils.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryFireIncrement implements FireIncrement {

    private final Map<String,AtomicLong> map = Maps.newHashMap();

    synchronized AtomicLong atomic(String name){
        AtomicLong atomic = map.get(name);
        if(atomic == null ){
            atomic = new AtomicLong();
            map.put(name,atomic);
        }
        return atomic;
    }
    @Override
    public void incr(String name) {
        atomic(name).incrementAndGet();
    }

    @Override
    public void incr(String name,long step) {
        atomic(name).addAndGet(step);
    }

    @Override
    public long get(String name) {
        return atomic(name).get();
    }

    @Override
    public void set(String name,long value) {
        atomic(name).set(value);
    }

    @Override
    public long incrGet(String name) {
        return atomic(name).incrementAndGet();
    }

    @Override
    public long incrGet(String name,long step) {
        return atomic(name).addAndGet(step);
    }

    @Override
    public long remove(String name) {
        AtomicLong item =  map.remove(name);
        if(item != null){
            return item.longValue();
        }
        return 0;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }
}
