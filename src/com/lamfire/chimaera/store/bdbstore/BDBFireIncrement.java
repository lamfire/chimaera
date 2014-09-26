package com.lamfire.chimaera.store.bdbstore;

import com.lamfire.chimaera.store.FireIncrement;
import com.lamfire.chimaera.store.Item;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.collections.StoredMap;

import java.util.concurrent.atomic.AtomicLong;


public class BDBFireIncrement implements FireIncrement {

    private BDBEngine engine;
    private String name;
    private StoredMap<String, AtomicLong> map;

    public class AtomicLongBinding extends TupleBinding<AtomicLong>{

        @Override
        public AtomicLong entryToObject(TupleInput tupleInput) {
            long value =  tupleInput.readLong();
            return new AtomicLong(value);
        }

        @Override
        public void objectToEntry(AtomicLong atomicLong, TupleOutput tupleOutput) {
            tupleOutput.writeLong(atomicLong.get());
        }
    }

    public BDBFireIncrement(BDBEngine engine, String name) {
        this.engine = engine;
        this.name = name;
        this.map = engine.getMap(name,new StringBinding(),new AtomicLongBinding());
    }

    private synchronized AtomicLong atomic(String name){
        AtomicLong atomic = map.get(name);
        if(atomic == null){
            atomic = new AtomicLong(0);
            map.put(name,atomic);
        }
        return atomic;
    }

    private synchronized void update(String name,AtomicLong atomic){
        map.put(name,atomic);
    }

    @Override
    public void incr(String name) {
        incrGet(name, 1);
    }

    @Override
    public void incr(String name, long step) {
        incrGet(name,step);
    }

    @Override
    public synchronized void set(String name, long value) {
        AtomicLong atomic = atomic(name);
        atomic.set(value);
        update(name,atomic);
    }

    @Override
    public long incrGet(String name) {
        return incrGet(name,1);
    }

    @Override
    public long incrGet(String name, long step) {
        AtomicLong atomic = atomic(name);
        long val = atomic.get();
        atomic.addAndGet(step);
        update(name,atomic);
        return val;
    }

    @Override
    public long get(String name) {
        return atomic(name).get();
    }

    @Override
    public synchronized long remove(String name) {
        AtomicLong atomic =  map.remove(name);
        if(atomic == null){
            return 0l;
        }
        return atomic.get();
    }

    @Override
    public long size() {
        return map.size();
    }

    @Override
    public synchronized void clear() {
        this.map.clear();
    }
}
